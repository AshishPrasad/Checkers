/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.Vector;

/**
 *
 * @author ASHISH
 */
public class Move {
    int initialRow;
    int initialCol;
    int finalRow;
    int finalCol;
    
    Move(int r1, int c1, int r2, int c2){
        this.initialRow = r1;
        this.initialCol = c1;
        this.finalRow = r2;
        this.finalCol = c2;        
    }
    
    public boolean Equals(Move move){       
        return (this.initialRow == move.initialRow 
                && this.initialCol == move.initialCol
                && this.finalRow == move.finalRow
                && this.finalCol == move.finalCol)?true:false;
    }
    
    public boolean ExistsInVector(Vector<Move> moves){        
        for (int i = 0; i<moves.size(); i++){
            if (this.Equals(moves.elementAt(i))){
                return true;
            }
        }
        return false;
    }
    
    public void display(){
        System.out.print("("+this.initialRow+","+this.initialCol+") -->"+" ("+this.finalRow+", "+this.finalCol+")");
    }
}
