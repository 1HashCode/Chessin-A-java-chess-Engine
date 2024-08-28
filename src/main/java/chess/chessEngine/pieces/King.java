package chess.chessEngine.pieces;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    /*the move jumps for the king is stored*/
    private final static int[] CANDIDATE_MOVE_COORDINATE={-9,-8,-7,-1,1,7,8,9};

    public King(int piecePosition, PieceColor pieceColor) {
        super(PieceType.KING,pieceColor, piecePosition,true);
    }

    public King(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.KING,pieceColor, piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move>legalMoves=new ArrayList<>();
        for(int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){
            //adding the jumps from the currentPosition
            int candidateDestinationCoordinate=this.piecePosition+currentCandidateOffset;

            if(ColumnException(currentCandidateOffset,this.piecePosition)){ //checking column exceptions
                continue;
            }
            //common logic for the move search
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
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    private boolean ColumnException(int coordinateOffset, int currentPosition) {
        boolean firstColumnException = BoardUtils.FIRST_COLUMN[currentPosition] && (coordinateOffset == -9 || coordinateOffset == -1
                || coordinateOffset == 6 || coordinateOffset == 7);

        boolean eightColumnException = BoardUtils.EIGHT_COLUMN[currentPosition] && (coordinateOffset == -7 || coordinateOffset == 1
                || coordinateOffset == 9);

        return firstColumnException || eightColumnException;
    }
}
