package chess.chessEngine.pieces;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    //the queen class is combination of rook and bishop and the offsets are acc to them
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATES={-9,-7,7,9,-8,-1,1,8};

    public Queen(int piecePosition, PieceColor pieceColor) {
        super(PieceType.QUEEN,pieceColor, piecePosition,true);
    }

    public Queen(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.QUEEN,pieceColor, piecePosition,isFirstMove);
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
        return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }

    private boolean ColumnException(int coordinateOffset, int currentPosition) {
        boolean firstColumnException = BoardUtils.FIRST_COLUMN[currentPosition] && (coordinateOffset == -9 || coordinateOffset == 7 || coordinateOffset == -1);

        boolean eightColumnException = BoardUtils.EIGHT_COLUMN[currentPosition] && (coordinateOffset == -7 || coordinateOffset == 9 || coordinateOffset == 1);

        return firstColumnException || eightColumnException;
    }
}
