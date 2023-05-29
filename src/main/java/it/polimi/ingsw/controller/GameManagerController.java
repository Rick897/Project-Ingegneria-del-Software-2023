package it.polimi.ingsw.controller;


import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.controllerObservers.GameManagerViewObserver;
import it.polimi.ingsw.controller.pubSub.Subscriber;
import it.polimi.ingsw.controller.pubSub.TopicType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.ChatMessage;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.MatchStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.modelSupport.Client;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
import it.polimi.ingsw.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManagerController extends Controller implements GameManagerViewObserver, Subscriber {
    private VirtualGameManager virtualGameManager;
    private loginGameMessage lastLoginMessage;
    private Thread lastThread;
    public GameManagerController(View view, VirtualGameManager virtualGameManager) {
        super(view);
        this.virtualGameManager = virtualGameManager;
        ClientManager.pubsub.addSubscriber(TopicType.gameManagerState, this);
        ClientManager.pubsub.addSubscriber(TopicType.errorMessageState, this);
        ClientManager.pubsub.addSubscriber(TopicType.networkMessageState, this);
        virtualGameManager.ping();
    }

    @Override
    public void onSetCredentials(String username, String password) {
        System.out.println("Credentials set");
        ClientManager.userNickname = username;
        virtualGameManager.setCredentials(username, password);
    }

    @Override
    public void onSelectGame(String gameId, String user) {
        virtualGameManager.selectGame(gameId, user);
    }

    @Override
    public void onCreateGame(int numOfPlayers, String user) {
        virtualGameManager.createGame(numOfPlayers, user);
    }

    @Override
    public void onLookForNewGames(String user){
        virtualGameManager.lookForNewGames(user);
    }

    @Override
    public boolean receiveSubscriberMessages(Message message) {
        if(message instanceof NetworkMessage){
            //this message holds Messages useful for network
            switch (((NetworkMessage) message).message){
                case "pong":
                    System.out.println(ClientManager.gameManagerController);
                    System.out.println("pong received");
                    ClientManager.view.requestCredentials();
                    break;
                default:
                    break;
            }
        }else if(message instanceof ErrorMessage mess){
            switch (mess.error.toString()) {
                case "wrongPassword":
                    //System.out.println("error case in GameManagerController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.requestCredentials();
                    break;
                case "lobbyIsFull":
                    //System.out.println("error case in GameManagerController: "+mess.info);
                    ClientManager.view.showErrorMessage(mess.info);
                    ClientManager.view.launchGameManager(lastLoginMessage.gamesPlayers);
                    break;
            }
        }else if(message instanceof loginGameMessage mess){
            //user can go in, launchGameManager phase
            if(lastLoginMessage == null){
                this.lastLoginMessage = mess;
                ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);
            }else{
                //this.lastLoginMessage = (loginGameMessage)message;
                int allGamesSize = mess.gamesPlayers.keySet().toArray().length;
                String addedGameId = mess.gamesPlayers.keySet().toArray()[allGamesSize-1].toString();
                List<String> addedGamePlayers = mess.gamesPlayers.get(addedGameId);
                Pair<String, List<String>> addedGame;
                //Check needed to add a new game or update an old one with a new player entry
                if(lastLoginMessage.gamesPlayers.containsKey(addedGameId)){
                    boolean found = false;
                    for(String key: mess.gamesPlayers.keySet()){
                        for(String playerName: mess.gamesPlayers.get(key)){
                            if(!lastLoginMessage.gamesPlayers.get(key).contains(playerName)){
                                addedGameId = key;
                                addedGamePlayers = mess.gamesPlayers.get(key);
                                found = true;
                                break;
                            }
                        }
                        if(found){
                            break;
                        }
                    }
                }
                addedGame = new Pair<>(addedGameId,addedGamePlayers);
                ClientManager.view.addNewGame(addedGame);

            }



            /*if(lastThread != null){
                System.out.println("launchGameManager "+lastThread.getName()+" interrupted");
                lastThread.interrupt();
            }else{
                System.out.println("First launchGameManager");
            }
            this.lastThread = Thread.currentThread();
            System.out.println("launchGameManager Thread name: "+Thread.currentThread().getName());
            ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);
        }else if(message instanceof ChatMessage){
            ClientManager.view.newChatMessage(((ChatMessage) message).messages);
            ClientManager.view.launchGameManager(this.lastLoginMessage.gamesPlayers);*/
        }
        return true;
    }
}
