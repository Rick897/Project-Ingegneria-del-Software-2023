package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CommonGoals implements Serializable {
    /**
     * It refers to the first goal chosen
     */
    private CommonGoalStrategy firstGoal;
    /**
     * It refers to the second goal chosen
     */
    private CommonGoalStrategy secondGoal;
    /**
     * It consists in the ordered list of players that have reached the first goal
     */
    private final ArrayList<Player> reachedFirstGoal = new ArrayList<>();
    /**
     * It consists in the ordered list of players that have reached the second goal
     */
    private final ArrayList<Player> reachedSecondGoal = new ArrayList<>();
    /**
     * Creator of CommonGoals: chooses randomly two goal strategies out of 12 of the enumeration
     */
    public CommonGoals() {
        int[] indexes = ThreadLocalRandom.current().ints(0, 12).distinct().limit(2).toArray();
        switch (indexes[0]){
            case 0:
                firstGoal = new SixOfTwoGoalStrategy();
                break;
            case 1:
                firstGoal = new FiveDiagonalGoalStrategy();
                break;
            case 2:
                firstGoal = new FourOfFourGoalStrategy();
                break;
            case 3:
                firstGoal = new FourCornersGoalStrategy();
                break;
            case 4:
                firstGoal = new Double2x2GoalStrategy();
                break;
            case 5:
                firstGoal = new ThreeColumns3DiffGoalStrategy();
                break;
            case 6:
                firstGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                firstGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                firstGoal = new TwoOf6DiffGoalStrategy();
                break;
            case 9:
                firstGoal = new TwoOf5DiffGoalStrategy();
                break;
            case 10:
                firstGoal = new FiveXGoalStrategy();
                break;
            case 11:
                firstGoal = new TriangularGoalStrategy();
        }
        switch (indexes[1]){
            case 0:
                secondGoal = new SixOfTwoGoalStrategy();
                break;
            case 1:
                secondGoal = new FiveDiagonalGoalStrategy();
                break;
            case 2:
                secondGoal = new FourOfFourGoalStrategy();
                break;
            case 3:
                secondGoal = new FourCornersGoalStrategy();
                break;
            case 4:
                secondGoal = new Double2x2GoalStrategy();
                break;
            case 5:
                secondGoal = new ThreeColumns3DiffGoalStrategy();
                break;
            case 6:
                secondGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                secondGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                secondGoal = new TwoOf6DiffGoalStrategy();
                break;
            case 9:
                secondGoal = new TwoOf5DiffGoalStrategy();
                break;
            case 10:
                secondGoal = new FiveXGoalStrategy();
                break;
            case 11:
                secondGoal = new TriangularGoalStrategy();
        }
    }
    /**
     * Getter for the first commonGoal
     * @return CommonGoalStrategy
     */
    public CommonGoalStrategy getFirstGoal() {
        return firstGoal;
    }
    /**
     * Getter for the second commonGoal
     * @return CommonGoalStrategy
     */
    public CommonGoalStrategy getSecondGoal() {
        return secondGoal;
    }
    /**
     * Sets the firstGoal desired, it is used for the testing section
     * @param firstGoal
     */
    public void setFirstGoal(CommonGoalStrategy firstGoal) {
        this.firstGoal = firstGoal;
    }
    /**
     * Sets the secondGoal desired, it is used for the testing section
     * @param secondGoal
     */
    public void setSecondGoal(CommonGoalStrategy secondGoal) {
        this.secondGoal = secondGoal;
    }
    /**
     * Calculates player's points based on the number of players and the goals' completion
     * @param player
     * @param numOfPlayers
     * @return int
     */
    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris;
        int pointsOfFirst;
        int pointsOfSecond;

        if(!reachedFirstGoal.contains(player)){
            if (firstGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {
                reachedFirstGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 2);
                } else {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 4);
                }
            } else {
                pointsOfFirst = 0;
            }
        }else{
            pointsOfFirst = 0;
        }
        if(!reachedSecondGoal.contains(player)){
            if (secondGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {
                reachedSecondGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 2);
                } else {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 4);
                }
            } else {
                pointsOfSecond = 0;
            }
        }else{
            pointsOfSecond = 0;
        }
        ris = pointsOfFirst + pointsOfSecond;
        return ris;
    }
}
