package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.GameManagerMessages.loginGameMessage;
import it.polimi.ingsw.model.messageModel.NetworkMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.matchStateMessages.GameStateMessage;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton with all the current games and the needed methods to create or join a game
 */
public class GameManager extends GameObservable{

    //this instance is used whenever we want the singleton
    private static GameManager instance;

    //hash map with the game lobby and the relative games
    private HashMap<GameLobby, Game> currentGames;
    private HashMap<String, String> nicknames;

    private HashMap<String, Game> userMatches;
    private GameManager(){
        nicknames = new HashMap<>();
        currentGames = new HashMap<>();
        userMatches = new HashMap<>();
    }

    public void selectGame(String ID, String user){
        //currentGames.put(new GameLobby());
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID) && !x.isKilled()){
                //joins this lobby
                try {
                    x.addPlayer(user);
                }catch(LobbyFullException e){
                    //lobby is full, returns error

                }
            }
        }
    }


    public void createMatchFromLobby(String withID, ArrayList<String> withPlayers){
        if(!currentGames.containsKey(withID)){
            //handle error id so not exist
            return;
        }
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(withID)){
                ArrayList<Player> players = new ArrayList<>();
                for(String p: withPlayers){
                    players.add(new Player(p));
                }
                currentGames.put(x, new Game(players, withID));
                x.killLobby();
                //game has been created
            }
        }

    }

    public void ping(){
        //received ping message
        //send pong
        //TODO: server.send(new NetworkMessage("pong"));
    }

    public void createGame(int numPlayers){

    }


    /**
     * Gets all the lobbies that have not been tombstoned (joinable)
     * @return
     */
    private HashMap<GameLobby, Game> getAllCurrentJoinableLobbies(){
        HashMap<GameLobby, Game> out = new HashMap<>();
        for(GameLobby x: this.currentGames.keySet()){
            if(!x.isKilled()){
                out.put(x, this.currentGames.get(x));
            }
        }
        return out;
    }

    public void setCredentials(String username, String password){
        //check if there was, else send message of erroneus urername set request.
        if(nicknames.containsKey(username)){
            //already exists, checks if psw is right
            if (nicknames.get(username).equals(password)){
                //ok login
                //sends all the games
                super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbies()), false, "-");
            }else{
                //username wrong password
                //sends error
                //super.notifyObserver(username, new ErrorMessage(ErrorType.wrongPassword));
            }
        }else{
            //new user
            nicknames.put(username, password);
            super.notifyObserver(username, new loginGameMessage(getAllCurrentJoinableLobbies()), false, "-");
        }
    }

    /*
    Thread safe GameManager instance creator
     */
    public static synchronized GameManager getInstance() {
            synchronized (GameManager.class) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        return instance;
    }

    /*
    public void createGame() throws ErrorInGameCreationException{

    }
     */


    /*
    Gest methods LOBBIES and forward them to the exact game and lobby
     */

    public void startMatch(String ID, String user){
        for(GameLobby x: currentGames.keySet()){
            if(x.getID().equals(ID)){
                x.startMatch(user);
            }
        }
    }


}
