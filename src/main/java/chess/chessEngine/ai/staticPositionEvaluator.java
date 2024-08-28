package chess.chessEngine.ai;

import chess.chessEngine.board.Board;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.player.Player;

public final class staticPositionEvaluator {
    private static final int CHECK_BONUS = 40;
    private static final int CHECK_MATE_BONUS =10000;
    private static final int DEPTH_BONUS=100;
    private static final int CASTLE_BONUS=130;

    private static final int[] chessboardImportanceOfEachTile =
                    {5, 10, 15, 20, 20, 15, 10, 5,    // Row 1 (a1 to h1)
                    10, 15, 20, 30, 30, 20, 15, 10,  // Row 2 (a2 to h2)
                    15, 20, 30, 35, 35, 30, 20, 15,  // Row 3 (a3 to h3)
                    20, 30, 35, 45, 45, 35, 30, 20,  // Row 4 (a4 to h4)
                    20, 30, 35, 45, 45, 35, 30, 20,  // Row 5 (a5 to h5)
                    15, 20, 30, 35, 35, 30, 20, 15,  // Row 6 (a6 to h6)
                    10, 15, 20, 30, 30, 20, 15, 10, // Row 7 (a7 to h7)
                    5, 10, 15, 20, 20, 15, 10, 5} ;    // Row 8 (a8 to h8)



    public int evaluate(final Board board, int depth){
        return scorePlayer(board,board.whitePlayer(),depth)-scorePlayer(board,board.blackPlayer(),depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth){
        return pieceValue(player)+mobility(player)+check(player)+checkmate(player,depth)+castled(player)+evaluatePositionalPieceValue(board);
    }

    private int evaluatePositionalPieceValue(final Board board){
        int finalValue=0;
        for(Piece piece:board.currentPlayer().getActivePieces()){
            int position=piece.getPiecePosition();
            finalValue+=chessboardImportanceOfEachTile[position];
        }
        return finalValue;
    }

    private static int castled(Player player) {
        return player.isCastled()?CASTLE_BONUS:0;
    }

    private static int checkmate(Player player,int depth) {
        return player.getOpponent().isInCheckMate()?CHECK_MATE_BONUS*depthBonus(depth):0;
    }

    private static int depthBonus(int depth){
        return depth==0?1:DEPTH_BONUS*depth;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck()?CHECK_BONUS:0;
    }

    private static int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player){
        int pieceValueScore=0;
        for(final Piece piece: player.getActivePieces()){
            pieceValueScore+=piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
