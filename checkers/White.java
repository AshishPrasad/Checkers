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
public class White
{   
    static Owner owner;
    
    public static void Move()
    {   
        UserInteractions.PrintSeparator('-');
        System.out.println("\t\tWHITE's TURN");
        UserInteractions.PrintSeparator('-');
        
        if (owner.equals(Owner.HUMAN)){            
            
            Human.makeNextWhiteMoves();
            
        }                
        else{
            
            assert(owner.equals(Owner.ROBOT));
            Robot.makeNextWhiteMoves();
            
        }
    }
    

    
    public static Vector<Move> ObtainForcedMovesForWhite(int r, int c, Board board) 
    {        
        Vector<Move> furtherCaptures = new Vector<Move>();        
        
        if (board.cell[r][c].equals(CellEntry.white) || board.cell[r][c].equals(CellEntry.whiteKing))                
        {
            if (ForwardLeftCaptureForWhite(r,c,board)!=null)
                furtherCaptures.add(ForwardLeftCaptureForWhite(r,c,board));
            if (ForwardRightCaptureForWhite(r,c,board)!=null)
                furtherCaptures.add(ForwardRightCaptureForWhite(r,c,board));
        }        
        
        if(board.cell[r][c].equals(CellEntry.whiteKing))
        {
            if (BackwardLeftCaptureForWhite(r,c,board)!=null)
                furtherCaptures.add(BackwardLeftCaptureForWhite(r,c,board));
            if (BackwardRightCaptureForWhite(r,c,board)!=null)
                furtherCaptures.add(BackwardRightCaptureForWhite(r,c,board));
        }
        
        return furtherCaptures;
    }

    public static Vector<Move> CalculateAllForcedMovesForWhite(Board board) 
    {        
        Vector<Move> forcedMovesForWhite = new Vector<Move>();
        
        // Scan across the board
        for(int r = 0; r<Board.rows; r++)
        {            
            // Check only valid cols
            int c = (r%2==0)?0:1;
            for(; c<Board.cols; c+=2)
            {       
                assert(!board.cell[r][c].equals(CellEntry.inValid));
                
                // Forward Capture
                if(
                        board.cell[r][c].equals(CellEntry.white) || 
                        board.cell[r][c].equals(CellEntry.whiteKing)
                  )
                {       
                    // Boundary Condition for forward capture
                    if (r<Board.rows-2)
                    {    
                        // Forward Left Capture
                        if (ForwardLeftCaptureForWhite(r,c, board)!=null)
                            forcedMovesForWhite.add(ForwardLeftCaptureForWhite(r,c, board));                        
                        
                        // Forward Right Capture
                        if (ForwardRightCaptureForWhite(r,c, board)!=null)
                            forcedMovesForWhite.add(ForwardRightCaptureForWhite(r,c, board));
                    }                   
                }
                // Backward Capture
                if(board.cell[r][c].equals(CellEntry.whiteKing))
                {
                    // Boundary Condition for backward capture
                    if (r>=2)
                    {          
                        // Backward Left Capture
                        if (BackwardLeftCaptureForWhite(r,c,board)!=null)
                            forcedMovesForWhite.add(BackwardLeftCaptureForWhite(r,c, board));
                        
                        // Backward Right Capture
                        if (BackwardRightCaptureForWhite(r,c,board)!=null)
                            forcedMovesForWhite.add(BackwardRightCaptureForWhite(r,c,board));                        
                    }
                }
            }    
        }
        
        return forcedMovesForWhite;
    }    

    
    
    
        /**
     * Returns a vector of all possible moves which White can make at the state of the game given by board.
     * 
     * Should only be called if no forced moves exist.
     * 
     * @param board
     * @return 
     */
    public static Vector<Move> CalculateAllNonForcedMovesForWhite(Board board){
        
        Vector<Move> allNonForcedMovesForWhite = new Vector<Move>();
        
        // Scan across the board
        for(int r = 0; r<Board.rows; r++)
        {
            // Check only valid cols
            int c = (r%2==0)?0:1;
            for(; c<Board.cols; c+=2)
            {      
                assert(!board.cell[r][c].equals(CellEntry.inValid));
                
                // Forward Move for normal white piece.
                if( board.cell[r][c].equals(CellEntry.white) ){

                    Move move = null;
                    move = ForwardLeftCaptureForWhite(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForWhite(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);   
                    }
                    
                    move = ForwardRightForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);
                    }
                }
                
                                //Forward and Backward Move for black king piece.
                if(board.cell[r][c] == CellEntry.whiteKing){                
                    Move move = null;
                    move = ForwardLeftCaptureForWhite(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForWhite(r, c, board);
                    assert(move == null);

                    move = BackwardLeftCaptureForWhite(r, c, board);
                    assert(move == null);
                    move = BackwardRightCaptureForWhite(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);
                    }
                    
                    move = ForwardRightForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);
                    }
                    
                    move = BackwardLeftForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);
                    }
                    
                    move = BackwardRightForWhite(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForWhite.add(move);
                    }
                    
                }


            } 
        }
        
        return allNonForcedMovesForWhite;
    }

    
    
    private static Move ForwardLeftForWhite(int r, int c, Board board){
        Move forwardLeft = null;
        if( r<Board.rows-1 && c>=1 &&
                board.cell[r+1][c-1] == CellEntry.empty
          )
        {
            forwardLeft = new Move(r,c, r+1, c-1);
        }
        return forwardLeft;
    }
    
    // Forward Left Capture for White
    private static Move ForwardLeftCaptureForWhite(int r, int c, Board board)
    {        
        Move forwardLeftCapture = null;
        
        if(r<Board.rows-2 && c>=2 &&
                (
                board.cell[r+1][c-1].equals(CellEntry.black)
                || board.cell[r+1][c-1].equals(CellEntry.blackKing)
                )
                && board.cell[r+2][c-2].equals(CellEntry.empty)
                )
        {
             forwardLeftCapture = new Move(r,c,r+2,c-2);
             //System.out.println("Forward Left Capture");
        }        
        
        return forwardLeftCapture;
    }
    
    private static Move ForwardRightForWhite(int r, int c, Board board){
        Move forwardRight = null;
        if(r<Board.rows-1 && c<Board.cols-1 &&
                board.cell[r+1][c+1] == CellEntry.empty
          )
        {
            forwardRight = new Move(r,c, r+1, c+1);
        }
        return forwardRight;
    }
    
    // Forward Right Capture for White
    private static Move ForwardRightCaptureForWhite(int r, int c, Board board)
    {        
        Move forwardRightCapture = null;
        
        if(r<Board.rows-2 && c<Board.cols-2 &&
                (
                board.cell[r+1][c+1].equals(CellEntry.black)
                || board.cell[r+1][c+1].equals(CellEntry.blackKing)
                )
                && board.cell[r+2][c+2].equals(CellEntry.empty)
                )
        {
            forwardRightCapture = new Move(r,c,r+2,c+2);
            //System.out.println("Forward Right Capture");
        }
        
        return forwardRightCapture;
    }
    
    private static Move BackwardLeftForWhite(int r, int c, Board board){
        Move backwardLeft = null;
        if( r>=1 && c>=1 &&
                board.cell[r-1][c-1] == CellEntry.empty
          )
        {
            backwardLeft = new Move(r,c, r-1, c-1);
        }
        return backwardLeft;
    }
    
     // Backward Left Capture for White
    private static Move BackwardLeftCaptureForWhite(int r, int c, Board board)
    {
        
        Move backwardLeftCapture = null;
        
        if(r>=2 && c>=2 && (
                board.cell[r-1][c-1].equals(CellEntry.black)
                || board.cell[r-1][c-1].equals(CellEntry.blackKing)
                )
                && board.cell[r-2][c-2].equals(CellEntry.empty)
                )
        {
             backwardLeftCapture = new Move(r,c,r-2,c-2);
             //System.out.println("Backward Left Capture");
        }
        
        return backwardLeftCapture;
    }
    
    private static Move BackwardRightForWhite(int r, int c, Board board){
        Move backwardRight = null;
        if(r>=1 && c<Board.cols-1 &&
                board.cell[r-1][c+1] == CellEntry.empty
          )
        {
            backwardRight = new Move(r,c,r-1,c+1);
        }
        return backwardRight;
    }
    // Backward Right Capture for White
    private static Move BackwardRightCaptureForWhite(int r, int c, Board board)
    {        
        Move backwardRightCapture = null;
        
        if(r>=2 && c<Board.cols-2 && (
                board.cell[r-1][c+1].equals(CellEntry.black) ||
                board.cell[r-1][c+1].equals(CellEntry.blackKing)
                )
                && board.cell[r-2][c+2].equals(CellEntry.empty)
                )
        {
            backwardRightCapture = new Move(r,c,r-2,c+2);
            //System.out.println("Backward Right Capture");
        }
        
        return backwardRightCapture;
    }
}