/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import java.util.Vector;

/** 
 * @author ASHISH
 */
public class Board {

    int blackPieces;
    int whitePieces;
    
    static final int rows = 8;
    static final int cols = 8;
    CellEntry cell[][];
    
    Board(){
        this.blackPieces = this.whitePieces = 12;        
        
        this.cell = new CellEntry[][]{                        
            {CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
            {CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white},
            {CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
            {CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
            {CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},            
            {CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black},
            {CellEntry.black,CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
            {CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black}
        };        
    }
    
    Board(CellEntry[][] board){        
        this.blackPieces = this.whitePieces = 12;        
        
        this.cell = new CellEntry[rows][cols];
        for(int i = 0; i<rows; i++){            
            System.arraycopy(board[i], 0, this.cell[i], 0, cols);
        }
    }
    
    public void MakeMove(int r1, int c1, int r2, int c2) 
    {        
        this.cell[r2][c2] = this.cell[r1][c1];
        this.cell[r1][c1] = CellEntry.empty;
        
        // Promote To King
        if(this.cell[r2][c2].equals(CellEntry.white) && r2==rows-1){
            this.cell[r2][c2] = CellEntry.whiteKing;
        }
        else if(this.cell[r2][c2].equals(CellEntry.black) && r2==0){
            this.cell[r2][c2] = CellEntry.blackKing;
        }
    }
    
    // Capture Black Piece and Move
    public void CaptureBlackPiece(int r1, int c1, int r2, int c2) 
    {  
        // Check Valid Capture
        assert(Math.abs(r2-r1)==2 && Math.abs(c2-c1)==2);
        
        // Obtain the capture direction
        MoveDir dir = r2>r1?(c2>c1?MoveDir.forwardRight:MoveDir.forwardLeft)
                :(c2>c1?MoveDir.backwardRight:MoveDir.backwardLeft);
        
        // Removing Black Piece from the board
        switch(dir){
            case forwardLeft:
                this.cell[r1+1][c1-1] = CellEntry.empty;
                break;
            case forwardRight:
                this.cell[r1+1][c1+1] = CellEntry.empty;
                break;
            case backwardLeft:
                this.cell[r1-1][c1-1] = CellEntry.empty;
                break;
            case backwardRight:
                this.cell[r1-1][c1+1] = CellEntry.empty;
                break;
        }
        
        // Decreasing the count of black pieces
        this.blackPieces--;
        
        // Making move
        this.MakeMove(r1, c1, r2, c2);
        //return dir;
    }
    
    // Capture White Piece and Move
    public void CaptureWhitePiece(int r1, int c1, int r2, int c2) 
    {  
        // Check Valid Capture
        assert(Math.abs(r2-r1)==2 && Math.abs(c2-c1)==2);
        
        // Obtain the capture direction
        MoveDir dir = r2<r1?(c2<c1?MoveDir.forwardRight:MoveDir.forwardLeft)
                :(c2<c1?MoveDir.backwardRight:MoveDir.backwardLeft);
        
        // Removing White Piece from the board
        switch(dir){
            case forwardLeft:
                this.cell[r1-1][c1+1] = CellEntry.empty;
                break;
            case forwardRight:
                this.cell[r1-1][c1-1] = CellEntry.empty;
                break;
            case backwardLeft:
                this.cell[r1+1][c1+1] = CellEntry.empty;
                break;
            case backwardRight:
                this.cell[r1+1][c1-1] = CellEntry.empty;
                break;
        }
        
        // Decreasing the count of white pieces
        this.whitePieces--;
        
        // Making move
        this.MakeMove(r1, c1, r2, c2);
        //return dir;
    }
    
    /**
     * Makes all kinds of valid moves of a white player.
     * @param move 
     */
    public void genericMakeWhiteMove(Move move){
        int r1 = move.initialRow;
        int c1 = move.initialCol;
        int r2 = move.finalRow;
        int c2 = move.finalCol;
        
        if((Math.abs(r2-r1)==2 && Math.abs(c2-c1)==2)){
            CaptureBlackPiece(r1, c1, r2, c2);
            
        }else{
            MakeMove(r1, c1, r2, c2);
        }
    }
    
    /**
     * Makes all kinds of valid moves of a black player.
     */
    public void genericMakeBlackMove(Move move){
        int r1 = move.initialRow;
        int c1 = move.initialCol;
        int r2 = move.finalRow;
        int c2 = move.finalCol;
        
        if(Math.abs(r2-r1)==2 && Math.abs(c2-c1)==2){
            CaptureWhitePiece(r1, c1, r2, c2);
            
        }else{
            MakeMove(r1, c1, r2, c2);
        }

    }
    
    public void Display()
    {
        this.DisplayColIndex();
        this.DrawHorizontalLine();
        
        for(int i = rows-1; i >=0; i--)
        {
            this.DisplayRowIndex(i);           
            this.DrawVerticalLine();
            
            for(int j = 0; j< cols; j++)
            {
                System.out.print(this.BoardPiece(i,j));
                this.DrawVerticalLine();
            }   
            
            this.DisplayRowIndex(i);
            System.out.println();
            this.DrawHorizontalLine();
        }
        
        this.DisplayColIndex();
        System.out.println();
    }    

    private String BoardPiece(int i, int j) {
        assert(i>0 && i<rows && j>0 && j< cols);
        String str = new String();
        
        if(this.cell[i][j] == CellEntry.inValid){
            str = "     ";
        }
        else if(this.cell[i][j] == CellEntry.empty){
            str = "  _  ";
        }
        else if(this.cell[i][j] == CellEntry.white){
            str = "  W  ";
        }        
        else if(this.cell[i][j] == CellEntry.black){
            str = "  B  ";
        }
        else if(this.cell[i][j] == CellEntry.whiteKing){
            str = "  W+ ";
        }
        else if(this.cell[i][j] == CellEntry.blackKing){
            str = "  B+ ";
        }
        
        return str;
    }

    private void DrawHorizontalLine() {        
        System.out.println("    _______________________________________________");   
    }

    private void DrawVerticalLine() {
        System.out.print("|");
    }

    private void DisplayColIndex() {
        System.out.print("   ");
        for(int colIndex = 0; colIndex<cols; colIndex++){            
            System.out.print("   " + colIndex + "  " );            
        }
        System.out.println();
    }

    private void DisplayRowIndex(int i) {
        System.out.print(" " + i + " ");
    }
    
    
    public Board duplicate(){
        Board newBoard = new Board(this.cell);
        newBoard.blackPieces = this.blackPieces;
        newBoard.whitePieces = this.whitePieces;
        
        return newBoard;
    }
    
    
    public boolean CheckGameComplete() {
        return (this.blackPieces==0 || this.whitePieces == 0)?true:false;
    }
    
    
    public boolean CheckGameDraw(Player turn){
        
        Vector<Vector<Move>> possibleMoveSeq = Robot.expandMoves(this.duplicate(), turn);
        
        if(possibleMoveSeq.isEmpty()){
            return true;
            
        }else{
            return false;
        }
    }
    
    public boolean isWhiteWinner(){
        boolean res = false;
        if(this.blackPieces == 0){
            res = true;
        }
        return res;
    }
    
    public boolean isBlackWinner(){
        boolean res = false;
        if(this.whitePieces == 0){
            res = true;
        }
        return res;
    }
}