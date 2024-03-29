package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of TwoOf5Diff: it looks for 2 full lines with all the boardCards with a different color
 */
public class TwoOf5DiffGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of TwoOf5Diff
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){
   //possible implementation close to twoofsix


        /* Ho creato un set a partire dall'enum e per iterarci con un indice l'ho resa una lista */
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int correctLines = 0;

        for(int i = 0; i < Mat.length && completed == 0; i++){
            int valid = 1;
            colors.removeAll(colors);
            for(int j = 0; j < Mat[0].length; j++){
                if(Mat[i][j].getColor() != colorType.EMPTY_SPOT){
                    if(!colors.contains(Mat[i][j].getColor())){
                        colors.add(Mat[i][j].getColor());
                    }else{
                        valid = 0;
                    }
                }else{
                    valid = 0;
                }
            }
            if(valid == 1){
                correctLines++;
                if(correctLines == 2){
                    completed = 1;
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    }
    public String toStringCommonGoal() {
        return "TwoOf5DiffGoalStrategy";
    }
    public int getIndex(){
        return 6;
    }
}
