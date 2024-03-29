package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of EightTiles: it looks for 8 boardCards of the same color distributed in any manner
 */
public class EightTilesGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of EightTiles
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int [] numOfColor = {0,0,0,0,0,0};

        for(int i = 0; i < Mat.length && completed == 0; i++){
            for(int j = 0; j < Mat[0].length && completed == 0; j++) {
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT){
                    if(!colors.contains(Mat[i][j].getColor())){
                        colors.add(Mat[i][j].getColor());
                    }
                    numOfColor[colors.indexOf(Mat[i][j].getColor())]++;
                    if(numOfColor[colors.indexOf(Mat[i][j].getColor())] == 8){
                        completed = 1;
                    }
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns the name of the CommonGoal
     * @return String
     */
    public String toStringCommonGoal() {
        return "EightTilesGoalStrategy";
    }

    /**
     * Returns the index of the commongoal
     * @return int
     */
    public int getIndex(){
        return 9;
    }
}
