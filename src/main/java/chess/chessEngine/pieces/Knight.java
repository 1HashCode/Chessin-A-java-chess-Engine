package chess.chessEngine.pieces;

import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;
import chess.chessEngine.board.Tile;
import chess.chessEngine.pieceColor.PieceColor;


import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    /*the move jumps for the knight is stored*/
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final PieceColor pieceColor) {
        super(PieceType.KNIGHT,pieceColor, piecePosition,true);
    }

    public Knight(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.KNIGHT,pieceColor, piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
            //column exception
            if (ColumnException(currentCandidate, this.piecePosition)) {
                continue;
            }
            //adding the jumps from the currentPosition
            int candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            // same logic for move search
            if (BoardUtils.isValidTile(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                } else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    PieceColor pieceColor1 = pieceAtDestination.getPieceColor();
                    if (pieceColor1 != this.pieceColor) {
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private boolean ColumnException(int coordinateOffset, int currentPosition) {
        boolean firstColumnException = BoardUtils.FIRST_COLUMN[currentPosition] && ((coordinateOffset == -17) || (coordinateOffset == -10)
                || (coordinateOffset == 6) || (coordinateOffset == 15));

        boolean secondColumnException = BoardUtils.SECOND_COLUMN[currentPosition] && ((coordinateOffset == -10) || (coordinateOffset == 6));

        boolean seventhColumnException = BoardUtils.SEVENTH_COLUMN[currentPosition] && ((coordinateOffset == 10) || (coordinateOffset == -6));

        boolean eightColumnException = BoardUtils.EIGHT_COLUMN[currentPosition] && ((coordinateOffset == -15) || (coordinateOffset == 10)
                || (coordinateOffset == 17) || (coordinateOffset == -6));

        return firstColumnException || secondColumnException || eightColumnException || seventhColumnException;
    }
}
