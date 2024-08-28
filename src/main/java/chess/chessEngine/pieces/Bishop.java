package chess.chessEngine.pieces;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{

    /*the coordinates which needs to be added
    to the piecePosition successively for the movement of the piece*/
    private static final int[]CANDIDATE_MOVE_VECTOR_COORDINATES={-9,-7,7,9};

    public Bishop(int piecePosition, PieceColor pieceColor) {
        super(PieceType.BISHOP,pieceColor, piecePosition,true);
    }

    public Bishop(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.BISHOP,pieceColor, piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        List<Move>legalMoves=new ArrayList<>(); //evaluating legal moves
        for(int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){ //looping through the offsets
        int candidateDestinationCoordinate=this.piecePosition; //initialising a move
            while(BoardUtils.isValidTile(candidateDestinationCoordinate)){ // while the coordinate is valid in the board
                if(ColumnException(candidateCoordinateOffset,candidateDestinationCoordinate)){  //column exception
                    break;
                }
                candidateDestinationCoordinate+=candidateCoordinateOffset;  //successively adding the offsets
                if(BoardUtils.isValidTile(candidateDestinationCoordinate)){ // if its valid
                    Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate); // getting the tile
                    if(!candidateDestinationTile.isTileOccupied()){  //if the tile is not occupied
                        legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate)); //adding the move
                    }
                    else{
                        Piece pieceAtDestination = candidateDestinationTile.getPiece(); // getting the piece from the tile
                        PieceColor pieceColor = pieceAtDestination.getPieceColor();
                        // if the pieceColor is opposite add an attacking move
                        if (pieceColor != this.pieceColor) {
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
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    /*column exceptions are exceptions where if the current position is
    * encountered with the offset then illegal move generation takes place
    * thus we have to check it*/
    private boolean ColumnException(int coordinateOffset, int currentPosition) {
        boolean firstColumnException = BoardUtils.FIRST_COLUMN[currentPosition] && (coordinateOffset == -9 || coordinateOffset == 7);

        boolean eightColumnException = BoardUtils.EIGHT_COLUMN[currentPosition] && (coordinateOffset == -7 || coordinateOffset == 9);

        return firstColumnException || eightColumnException;
    }
}
