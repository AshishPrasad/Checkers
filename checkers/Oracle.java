package checkers;

/**
 *
 * @author apurv and ashish
 */
public class Oracle {

    public final int POINT_WON = 100000;
    public final int POINT_KING = 2000;
    public final int POINT_NORMAL = 1000;
    public final int POINT_CENTRAL_PIECE = 100;
    public final int POINT_END_PIECE = 50;
    public final int POINT_DEFENCE = 50;
    public final int POINT_ATTACK_NORMAL = 30;
    public final int POINT_ATTACK_KING = 60;

    public int evaluateBoard(Board board, Player player) {
        int boardValue = 0;

        if (player == Player.black) {
            boardValue = evaluateValueofBoardForBlack(board);
        } else {
            boardValue = evaluateValueofBoardForWhite(board);
        }

        return boardValue;
    }

    private int evaluateValueofBoardForWhite(Board board) {

        int wValue = 0;
        if (board.isWhiteWinner()) {
            wValue += POINT_WON;
            return wValue;
        } else {
            wValue = WhiteBlackPiecesDifferencePoints(board);
//            wValue += BoardPositionPoints(board);
            wValue /= board.blackPieces;
        }

        return wValue;
    }

    private int evaluateValueofBoardForBlack(Board board) {

        int bValue = 0;
        if (board.isBlackWinner()) {
            bValue -= POINT_WON;
            return bValue;
        } else {
            bValue = WhiteBlackPiecesDifferencePoints(board);
//            bValue += BoardPositionPoints(board);
            bValue /= board.whitePieces;
        }

        return bValue;
    }

    private int WhiteBlackPiecesDifferencePoints(Board board) {
        
        int value = 0;
        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));
                CellEntry entry = board.cell[r][c];

                if (entry == CellEntry.white) {
                    value += POINT_NORMAL;
                } else if (entry == CellEntry.whiteKing) {
                    value += POINT_KING;
                } else if (entry == CellEntry.black) {
                    value -= POINT_NORMAL;
                } else if (entry == CellEntry.blackKing) {
                    value -= POINT_KING;
                }
            }
        }
        return value;
    }

    private int BoardPositionPoints(Board board) {

        int value = 0;
        
        // Central Points
        if (board.cell[3][3] == CellEntry.white || board.cell[3][3] == CellEntry.whiteKing 
                || board.cell[3][5] == CellEntry.white || board.cell[3][5] == CellEntry.whiteKing) {
            value += POINT_CENTRAL_PIECE;
        }
        if (board.cell[4][2] == CellEntry.black || board.cell[4][2] == CellEntry.blackKing
                || board.cell[4][4] == CellEntry.black || board.cell[4][4] == CellEntry.blackKing) {
            value -= POINT_CENTRAL_PIECE;
        }
        
        // End Points
        if (board.cell[0][2] == CellEntry.white || board.cell[0][4] == CellEntry.white || board.cell[0][6] == CellEntry.white){
            value += POINT_END_PIECE;
        }
        
        if (board.cell[7][1] == CellEntry.black || board.cell[7][3] == CellEntry.black || board.cell[7][5] == CellEntry.black){
            value -= POINT_END_PIECE;
        }
                
        return value;
    }

    //Calculate points for attacking white piece.
    //To be called only by on a cell which has a black piece.    
    private int calcPointsFAforBlack(Board board, int r, int c) {

        CellEntry entry = board.cell[r][c];
        assert ((entry == CellEntry.black || entry == CellEntry.blackKing));
        int points = 0;

        if (r > 0) {
            if (c < Board.cols - 1) {
                if ((board.cell[r - 1][c + 1] == CellEntry.white || board.cell[r - 1][c + 1] == CellEntry.whiteKing)
                        && r < Board.rows - 1 && c >= 1 && board.cell[r + 1][c - 1] == CellEntry.empty) {
                    points -= POINT_ATTACK_NORMAL;
                }
                //TODO: Add backward attack for black king.

            }

            if (c > 0) {
                //Attack Points
                if ((board.cell[r - 1][c - 1] == CellEntry.white || board.cell[r - 1][c - 1] == CellEntry.whiteKing)
                        && r < Board.rows - 1 && c < Board.cols - 1 && board.cell[r + 1][c + 1] == CellEntry.empty) {
                    points -= POINT_ATTACK_NORMAL;
                }
            }
        }
        return points;
    }

    //Calculate points for defending black.
    private int calcPointsFDforBlack(Board board, int r, int c) {

        CellEntry entry = board.cell[r][c];
        assert ((entry == CellEntry.black || entry == CellEntry.blackKing));
        int points = 0;

        if (r == Board.rows - 1) {
            points += POINT_DEFENCE;
        } else if (r == Board.rows - 3
                && ((c < Board.cols - 2 && board.cell[Board.rows - 1][c + 2] == CellEntry.black && board.cell[Board.rows - 2][c + 1] == CellEntry.empty)
                || (c >= 2 && board.cell[Board.rows - 1][c - 2] == CellEntry.black && board.cell[Board.rows - 2][c - 1] == CellEntry.empty))) {
            points += POINT_DEFENCE;
        } else if (r < Board.rows - 1) {

            if (c < Board.cols - 1) {
                if (board.cell[r + 1][c + 1] == CellEntry.black || board.cell[r + 1][c + 1] == CellEntry.blackKing) {
                    points += POINT_DEFENCE;
                }

            }

            if (c > 0) {
                //Defense points
                if (board.cell[r + 1][c - 1] == CellEntry.black || board.cell[r + 1][c - 1] == CellEntry.blackKing) {
                    points += POINT_DEFENCE;
                }
            }

        }
        return points;
    }

    private int calcPointsFAforWhite(Board board, int r, int c) {
        CellEntry entry = board.cell[r][c];
        assert ((entry == CellEntry.white || entry == CellEntry.whiteKing));
        int points = 0;

        if (r < Board.rows - 1) {
            if (c < Board.cols - 1) {

                //Debit points for unsafe attack.
                if (board.cell[r + 1][c + 1] == CellEntry.black || board.cell[r + 1][c + 1] == CellEntry.blackKing) {
                    points -= POINT_ATTACK_NORMAL;
                }

            }
            if (c > 0) {

                //Attack Points.
                if (board.cell[r + 1][c - 1] == CellEntry.black || board.cell[r + 1][c - 1] == CellEntry.blackKing) {
                    points += POINT_ATTACK_NORMAL;
                }

            }
        }

        return points;
    }

    private int calcPointsFDforWhite(Board board, int r, int c) {

        CellEntry entry = board.cell[r][c];
        assert ((entry == CellEntry.white || entry == CellEntry.whiteKing));
        int points = 0;

        if (r == 0) {
            points += POINT_DEFENCE;
        } else if (r == 2
                && ((c < Board.cols - 2 && board.cell[2][c + 2] == CellEntry.white && board.cell[1][c + 1] == CellEntry.empty)
                || (c >= 2 && board.cell[0][c - 2] == CellEntry.black && board.cell[1][c - 1] == CellEntry.empty))) {
            points += POINT_DEFENCE;
        } else if (r >= 2) {
            if (c < Board.cols - 1) {

                //Defense Points
                if (board.cell[r - 1][c + 1] == CellEntry.white || board.cell[r - 1][c + 1] == CellEntry.whiteKing) {
                    points += POINT_DEFENCE;
                }
            }
            if (c > 0) {

                if (board.cell[r - 1][c - 1] == CellEntry.white || board.cell[r - 1][c - 1] == CellEntry.whiteKing) {
                    points += POINT_DEFENCE;
                }
            }
        }

        return points;
    }
}