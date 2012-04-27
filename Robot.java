package checkers;

import java.util.Vector;

/**
 *
 * @author apurv
 */
public class Robot {
    
    static Oracle oracle = new Oracle();
    static int MAX_DEPTH = 6;
    
    public static void makeNextWhiteMoves(){
        
        Vector<Move> resultantMoveSeq = new Vector<Move>();
        
        alphaBeta(Game.board, Player.white, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);
        
        //Apply the move to the game board.
        for(Move m:resultantMoveSeq){
            Game.board.genericMakeWhiteMove(m);
        }
        
        System.out.print("Robot's Move was ");
        UserInteractions.DisplayMoveSeq(resultantMoveSeq);
        System.out.println();
    }
    
    
    public static void makeNextBlackMoves(){

        Vector<Move> resultantMoveSeq = new Vector<Move>();
        
        alphaBeta(Game.board, Player.black, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);

        //Apply the move to the game board.
        for(Move m:resultantMoveSeq){
            Game.board.genericMakeBlackMove(m);
        }

        System.out.print("Robot's Move was ");
        UserInteractions.DisplayMoveSeq(resultantMoveSeq);
        System.out.println();


    }

    /**
     * White represents the maximizing player, he/she wants to maximize the value of the board.
     * Black is the minimizing player, he/she wants to minimize the value of the board.
     * 
     * alpha represents the maximum value that the max player is assured of, initially -inf.
     * beta represents the minimum value that the min player is assured of, initially +inf.
     * 
     * if(alpha>beta) break
     * 
     * 
     */
    private static int alphaBeta(Board board, Player player, int depth, int alpha, int beta, Vector<Move> resultMoveSeq){
        
        if(!canExploreFurther(board, player, depth)){
            int value = oracle.evaluateBoard(board, player);
            return value;
        }
        
        Vector<Vector<Move>> possibleMoveSeq = expandMoves(board, player);
        Vector<Board> possibleBoardConf = getPossibleBoardConf(board, possibleMoveSeq, player);
        
        Vector<Move> bestMoveSeq = null;
        
        if(player == Player.white){            
            
            for(int i=0; i<possibleBoardConf.size(); i++){
                
                Board b = possibleBoardConf.get(i);
                Vector<Move> moveSeq = possibleMoveSeq.get(i);
                
                int value = alphaBeta(b, Player.black, depth+1, alpha, beta, resultMoveSeq);

                if(value > alpha){
                    alpha = value;
                    bestMoveSeq = moveSeq;
                }
                if(alpha>beta){
                    break;
                }
            }
            
            //If the depth is 0, copy the bestMoveSeq in the result move seq.
            if(depth == 0 && bestMoveSeq!=null){
                resultMoveSeq.addAll(bestMoveSeq);
            }

            return alpha;
            
        }else{
            assert(player == Player.black);
            
            for(int i=0; i<possibleBoardConf.size(); i++){
               
                Board b = possibleBoardConf.get(i);
                Vector<Move> moveSeq = possibleMoveSeq.get(i);
                
                int value = alphaBeta(b, Player.white, depth+1, alpha, beta, resultMoveSeq);
                if(value < beta){
                    bestMoveSeq = moveSeq;
                    beta = value;
                }
                if(alpha>beta){
                    break;
                }
            }
            //If the depth is 0, copy the bestMoveSeq in the result move seq.
            if(depth == 0 && bestMoveSeq!=null){
                resultMoveSeq.addAll(bestMoveSeq);
            }

            return beta;
        }        
    }
    
    public static Vector<Vector<Move>> expandMoves(Board board, Player player){                
        
        Vector<Vector<Move>> outerVector = new Vector<Vector<Move>>();
        
        if(player == Player.black){

            Vector<Move> moves = null;
            moves = Black.CalculateAllForcedMovesForBlack(board);
            
            if(moves.isEmpty()){
                moves = Black.CalculateAllNonForcedMovesForBlack(board);                                

                for(Move m:moves){
                    Vector<Move> innerVector = new Vector<Move>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }
                
            }else{
                for(Move m:moves){
                    
                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Move> innerVector = new Vector<Move>();
                    
                    innerVector.add(m);
                    
                    Board boardCopy = board.duplicate();
                    boardCopy.genericMakeBlackMove(m);
                    expandMoveRecursivelyForBlack(boardCopy, outerVector, innerVector, r, c);
                    
                    innerVector.remove(m);
                    
                }                
            }
            
        }else if(player == Player.white){
            
            Vector<Move> moves = null;
            
            moves = White.CalculateAllForcedMovesForWhite(board);
            if(moves.isEmpty()){
                moves = White.CalculateAllNonForcedMovesForWhite(board);
                for(Move m:moves){
                    Vector<Move> innerVector = new Vector<Move>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }
            }else{
                for(Move m:moves){
                    
                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Move> innerVector = new Vector<Move>();
                    
                    innerVector.add(m);
                    
                    Board boardCopy = board.duplicate();
                    boardCopy.genericMakeWhiteMove(m);
                    expandMoveRecursivelyForWhite(boardCopy, outerVector, innerVector, r, c);
                    
                    innerVector.remove(m);
                    
                }                
                
            }
        }
        return outerVector;
    }
    
    private static void expandMoveRecursivelyForWhite(Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c){
        
        Vector<Move> forcedMoves = White.ObtainForcedMovesForWhite(r, c, board);
        
        if(forcedMoves.isEmpty()){
            Vector<Move> innerCopy = (Vector<Move>)innerVector.clone();
            outerVector.add(innerCopy);
            return;
            
        }else{
            for(Move m:forcedMoves){
                
                Board boardCopy = board.duplicate();
                boardCopy.genericMakeWhiteMove(m);
                
                innerVector.add(m);
                expandMoveRecursivelyForWhite(boardCopy, outerVector, innerVector, m.finalRow, m.finalCol);
                innerVector.remove(m);
                
            }
        }

              
    }
    
    private static void expandMoveRecursivelyForBlack(Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c){
        
        Vector<Move> forcedMoves = Black.ObtainForcedMovesForBlack(r, c, board);
        
        if(forcedMoves.isEmpty()){
            Vector<Move> innerCopy = (Vector<Move>)innerVector.clone();
            outerVector.add(innerCopy);
            return;
            
        }else{
            for(Move m:forcedMoves){
                
                Board boardCopy = board.duplicate();
                boardCopy.genericMakeBlackMove(m);
                
                innerVector.add(m);
                expandMoveRecursivelyForBlack(boardCopy, outerVector, innerVector, m.finalRow, m.finalCol);
                innerVector.remove(m);
                
            }
        }
    }

    
private static boolean canExploreFurther(Board board, Player player, int depth){
        boolean res = true;
        if(board.CheckGameComplete()  || board.CheckGameDraw(player)){
            res = false;
        }
        if(depth == MAX_DEPTH){
            res = false;
        }
        return res;
    }
    
    
    public static Vector<Board> getPossibleBoardConf(Board board, Vector<Vector<Move>> possibleMoveSeq, Player player){
        Vector<Board> possibleBoardConf= new Vector<Board>();

        for(Vector<Move> moveSeq:possibleMoveSeq){
            Board boardCopy = board.duplicate();
            for(Move move:moveSeq){                
                if(player == Player.black){
                    boardCopy.genericMakeBlackMove(move);
                    
                }else{
                    boardCopy.genericMakeWhiteMove(move);
                }
            }
            possibleBoardConf.add(boardCopy);
            //System.out.println();
        }

        return possibleBoardConf;
    }
    
///////////////////////DEBUGGING PURPOSES/////////////////////////////////////////////////////////    
    
    public static void main(String[] args){        
//        
//        CellEntry[][] cell = new CellEntry[][]{
//{CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white},
//{CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty},
//{CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},            
//{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black},
//{CellEntry.black,CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black}
//            
//        };        
//        
//        Board board = new Board(cell);
//        
//        Vector<Vector<Move>> outerVector = expandMoves(board, Player.black);
//        
//        board.Display();
//        
//        for(Vector<Move> innerVector:outerVector){
//            for(Move move:innerVector){
//                move.display();
//                System.out.print(", ");
//            }
//            System.out.println();
//        }


//===================================================Checking Game Draw===============================      
//        CellEntry[][] cell = new CellEntry[][]{                        
//{CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black},
//{CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
//{CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},            
//{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white},
//{CellEntry.white,CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
//{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white}
//        };
//
//        Board board = new Board(cell);
//        System.out.println(board.CheckGameDraw(Player.white));
//====================================================================================================    
        
//        //Board board = new Board();
//        Vector<Move> resultantMoveSeq = new Vector<Move>();
//        
//        alphaBeta(board, Player.black, 0, Float.MIN_VALUE, Float.MAX_VALUE, resultantMoveSeq);
//        board.Display();
//        displayMovesInVector(resultantMoveSeq);
//        System.out.println(resultantMoveSeq.size());
        
        //testCase1();
        //testCase2();
        testCase3();

    }
    
    // For debugging
    private static void displayMovesInVector(Vector<Move> v){
        for(Move m:v){
            m.display();
            System.out.print(", ");
        }
        System.out.println();
    }
    
    
    private static void testCase1(){
       CellEntry[][] cell = new CellEntry[][]{
{CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white},
{CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},            
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty,CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
{CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black}
            
        };
        
       System.out.println(Float.MIN_VALUE);
       if(-600>Integer.MIN_VALUE){
           System.out.println("Lele maje");///////
       }
        
        Board board = new Board(cell);
        board.whitePieces = 7;
        board.blackPieces = 7;
        
        Vector<Move> resultantMoveSeq = new Vector<Move>();
//        
        alphaBeta(board, Player.black, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);
        board.Display();
        displayMovesInVector(resultantMoveSeq);
        
        
    }
    
    private static void testCase2(){
       CellEntry[][] cell = new CellEntry[][]{
{CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white},
{CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},            
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.black},
{CellEntry.empty,CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
{CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black}
            
        };
       
        Board board = new Board(cell);
        board.whitePieces = 5;
        board.blackPieces = 6;
        
        Board newBoard = board.duplicate();
        System.out.println(newBoard.CheckGameComplete());
        
        Vector<Move> resultantMoveSeq = new Vector<Move>();
//        
        alphaBeta(board, Player.black, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);
        board.Display();
        displayMovesInVector(resultantMoveSeq);
        
        //Apply the move to the game board.
        for(Move m:resultantMoveSeq){
            board.genericMakeWhiteMove(m);
        }

        resultantMoveSeq.clear();
        
        alphaBeta(board, Player.white, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);
        board.Display();
        displayMovesInVector(resultantMoveSeq);                
    }
        
    private static void testCase3(){
       CellEntry[][] cell = new CellEntry[][]{
{CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid},
{CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.white, CellEntry.inValid, CellEntry.empty, CellEntry.inValid},
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid},            
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.empty},
{CellEntry.empty,CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid},
{CellEntry.inValid, CellEntry.empty, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black, CellEntry.inValid, CellEntry.black}
       };
         
       Board board = new Board(cell);
       board.whitePieces = 5;
       board.blackPieces = 6;
       
        Vector<Move> resultantMoveSeq = new Vector<Move>();
//        
        alphaBeta(board, Player.black, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);
        board.Display();
        displayMovesInVector(resultantMoveSeq);
    }    
}