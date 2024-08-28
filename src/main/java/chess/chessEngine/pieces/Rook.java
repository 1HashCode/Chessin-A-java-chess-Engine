package chess.chessEngine.pieces;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.List;

/* same logic as bishop but the column exception
 and the coordinates are different*/

public class Rook extends Piece{
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATES={-8,-1,1,8};

    public Rook(int piecePosition, PieceColor pieceColor) {
        super(PieceType.ROOK,pieceColor, piecePosition,true);
    }
    public Rook(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.ROOK,pieceColor, piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        List<Move>legalMoves=new ArrayList<>();
        for(int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate=this.piecePosition;
            while(BoardUtils.isValidTile(candidateDestinationCoordinate)){
                if(ColumnException(candidateCoordinateOffset,candidateDestinationCoordinate)){
                    break;
                }
                candidateDestinationCoordinate+=candidateCoordinateOffset;
                if(BoardUtils.isValidTile(candidateDestinationCoordinate)){
                    Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                    }
                    else{
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        PieceColor pieceColor1 = pieceAtDestination.getPieceColor();
                        if (pieceColor1 != this.pieceColor) {
                            legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    private boolean ColumnException(int coordinateOffset, int currentPosition) {
        boolean firstColumnException = BoardUtils.FIRST_COLUMN[currentPosition] && (coordinateOffset == -1);

        boolean eightColumnException = BoardUtils.EIGHT_COLUMN[currentPosition] && (coordinateOffset == 1);

        return firstColumnException || eightColumnException;
    }
}
