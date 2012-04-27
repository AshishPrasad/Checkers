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
public class Black {
    
    static Owner owner;
    
    public static void Move(){
        
        UserInteractions.PrintSeparator('-');
        System.out.println("\t\tBLACK's TURN");
        UserInteractions.PrintSeparator('-');
        
        if (owner.equals(Owner.HUMAN)){            
            
            Human.makeNextBlackMoves();
            
        }
        else{
            
            assert(owner.equals(Owner.ROBOT));
            Robot.makeNextBlackMoves();
            
        }
    }
    
    
    
    public static Vector<Move> ObtainForcedMovesForBlack(int r, int c, Board board) 
    {        
        Vector<Move> furtherCaptures = new Vector<Move>();        
        
        if (board.cell[r][c].equals(CellEntry.black) || board.cell[r][c].equals(CellEntry.blackKing))                
        {
            if (ForwardLeftCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(ForwardLeftCaptureForBlack(r,c,board));
            if (ForwardRightCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(ForwardRightCaptureForBlack(r,c,board));
        }        
        
        if(board.cell[r][c].equals(CellEntry.blackKing))
        {
            if (BackwardLeftCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(BackwardLeftCaptureForBlack(r,c,board));
            if (BackwardRightCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(BackwardRightCaptureForBlack(r,c,board));
        }
        
        return furtherCaptures;
    }

    public static Vector<Move> CalculateAllForcedMovesForBlack(Board board) 
    {        
        Vector<Move> forcedMovesForBlack = new Vector<Move>();
        
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
                        board.cell[r][c].equals(CellEntry.black) || 
                        board.cell[r][c].equals(CellEntry.blackKing)
                  )
                {       
                    // Boundary Condition for forward capture for black
                    if (r>=2)
                    {    
                        // Forward Left Capture for black
                        if (ForwardLeftCaptureForBlack(r,c, board)!=null)
                            forcedMovesForBlack.add(ForwardLeftCaptureForBlack(r,c, board));                        
                        
                        // Forward Right Capture for black
                        if (ForwardRightCaptureForBlack(r,c, board)!=null)
                            forcedMovesForBlack.add(ForwardRightCaptureForBlack(r,c, board));
                    }                   
                }
                // Backward Capture for Black King
                if(board.cell[r][c].equals(CellEntry.blackKing))
                {
                    // Boundary Condition for backward capture
                    if (r<Board.rows-2)
                    {          
                        // Backward Left Capture for black king
                        if (BackwardLeftCaptureForBlack(r,c,board)!=null)
                            forcedMovesForBlack.add(BackwardLeftCaptureForBlack(r,c, board));
                        
                        // Backward Right Capture for black king
                        if (BackwardRightCaptureForBlack(r,c,board)!=null)
                            forcedMovesForBlack.add(BackwardRightCaptureForBlack(r,c,board));                        
                    }
                }
            }    
        }
        
        return forcedMovesForBlack;
    }
    
    /**
     * Returns a vector of all possible moves which Black can make at the state of the game given by board.
     * 
     * Should only be called if no forced moves exist.
     * 
     * @param board
     * @return 
     */
    public static Vector<Move> CalculateAllNonForcedMovesForBlack(Board board){
        Vector<Move> allNonForcedMovesForBlack = new Vector<Move>();

        // Scan across the board
        for(int r = 0; r<Board.rows; r++)
        {            
            // Check only valid cols
            int c = (r%2==0)?0:1;
            for(; c<Board.cols; c+=2)
            {       
                assert(!board.cell[r][c].equals(CellEntry.inValid));
                
                // Forward Move for normal black piece.
                if( board.cell[r][c].equals(CellEntry.black) ){

                    Move move = null;
                    move = ForwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForBlack(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);   
                    }
                    
                    move = ForwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                }
                
                //Forward and Backward Move for black king piece.
                if(board.cell[r][c] == CellEntry.blackKing){                    
                    Move move = null;
                    move = ForwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForBlack(r, c, board);
                    assert(move == null);

                    move = BackwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);
                    move = BackwardRightCaptureForBlack(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = ForwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = BackwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = BackwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                }

                
            }
        }

        return allNonForcedMovesForBlack;
    }

    
    
    
    
    private static Move ForwardLeftForBlack(int r, int c, Board board){
        Move forwardLeft = null;
        
        assert(board.cell[r][c] == CellEntry.black || board.cell[r][c] == CellEntry.blackKing);
        
        if( r>=1 && c<Board.cols-1 &&
                board.cell[r-1][c+1] == CellEntry.empty
                
          )
        {
            forwardLeft = new Move(r,c,r-1, c+1);
        }
        return forwardLeft;
    }
    
    // Forward Left Capture for Black
    private static Move ForwardLeftCaptureForBlack(int r, int c, Board board)
    {        
        Move forwardLeftCapture = null;
        
        if(r>=2 && c<Board.cols-2 &&
                (
                board.cell[r-1][c+1].equals(CellEntry.white)
                || board.cell[r-1][c+1].equals(CellEntry.whiteKing)
                )
                && board.cell[r-2][c+2].equals(CellEntry.empty)
                )
        {
             forwardLeftCapture = new Move(r,c,r-2,c+2);
             //System.out.println("Forward Left Capture for Black");
        }        
        
        return forwardLeftCapture;
    }
    
    
    
    //Forward Right for Black
    private static Move ForwardRightForBlack(int r, int c, Board board){
        Move forwardRight = null;
        if( r>=1 && c>=1 &&
                board.cell[r-1][c-1] == CellEntry.empty
          )
        {
            forwardRight = new Move(r,c, r-1, c-1);
        }
        return forwardRight;
    }
    
    // Forward Right Capture for White
    private static Move ForwardRightCaptureForBlack(int r, int c, Board board){
        
        Move forwardRightCapture = null;
        
        if(r>=2 && c>=2 && (
                board.cell[r-1][c-1].equals(CellEntry.white)
                || board.cell[r-1][c-1].equals(CellEntry.whiteKing)
                )
                && board.cell[r-2][c-2].equals(CellEntry.empty)
                )
        {
            forwardRightCapture = new Move(r,c,r-2,c-2);
            //System.out.println("Forward Right Capture for Black");
        }
        
        return forwardRightCapture;
    }
    
    private static Move BackwardLeftForBlack(int r, int c, Board board){
        Move backwardLeft = null;
        
        assert(board.cell[r][c].equals(CellEntry.blackKing));
        if(r<Board.rows-1 && c<Board.cols-1 &&
           board.cell[r+1][c+1] == CellEntry.empty
          )
        {
            backwardLeft = new Move(r,c, r+1, c+1);
        }
        
        return backwardLeft;
    }
    
     // Backward Left Capture for Black
    private static Move BackwardLeftCaptureForBlack(int r, int c, Board board){
        
        Move backwardLeftCapture = null;
        
        if(r<Board.rows-2 && c<Board.cols-2 && (
                board.cell[r+1][c+1].equals(CellEntry.white)
                || board.cell[r+1][c+1].equals(CellEntry.whiteKing)
                )
                && board.cell[r+2][c+2].equals(CellEntry.empty)
                )
        {
             backwardLeftCapture = new Move(r,c,r+2,c+2);
             //System.out.println("Backward Left Capture for Black");
        }
        
        return backwardLeftCapture;
    }
    
    
    private static Move BackwardRightForBlack(int r, int c, Board board){
        Move backwardRight = null;
        
        assert(board.cell[r][c].equals(CellEntry.blackKing));
        
        if(r<Board.rows-1 && c>=1 &&
           board.cell[r+1][c-1].equals(CellEntry.empty) 
          )
        {
            backwardRight = new Move(r,c, r+1, c-1);
        }
        return backwardRight;
    }
    
    
    // Backward Right Capture for Black
    private static Move BackwardRightCaptureForBlack(int r, int c, Board board){
        
        Move backwardRightCapture = null;
        
        if(r<Board.rows-2 && c>=2 && (
                board.cell[r+1][c-1].equals(CellEntry.white) ||
                board.cell[r+1][c-1].equals(CellEntry.whiteKing)
                )
                && board.cell[r+2][c-2].equals(CellEntry.empty)
                )
        {
            backwardRightCapture = new Move(r,c,r+2,c-2);
            //System.out.println("Backward Right Capture for Black");
        }
        
        return backwardRightCapture;
    }
}