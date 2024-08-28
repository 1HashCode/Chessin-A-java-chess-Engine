package chess.chessEngine.pieces;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    /*moves for the pawn including :
    double jump-16
    single step-8
    left corner for capture-7
    right corner for capture-9
    */
    private final static int[] CANDIDATE_MOVE_COORDINATE={8,16,7,9};

    public Pawn(int piecePosition, PieceColor pieceColor) {
        super(PieceType.PAWN,pieceColor, piecePosition,true);
    }

    public Pawn(int piecePosition, PieceColor pieceColor, boolean isFirstMove){
        super(PieceType.PAWN,pieceColor, piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move>legalMoves=new ArrayList<>();

        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){
            /*the current position is added with the current offset
            * but the direction depends on the pieceColor (black/white)
            * therefore we find the pieceColor and subtract or added with
            * according to the getDirection method*/
            final int candidateDestinationCoordinate=this.piecePosition+(this.getPieceColor().getDirection()*currentCandidateOffset);

            if(!BoardUtils.isValidTile(candidateDestinationCoordinate)){
                continue;
            }
            // single jump and the tile is not occupied
            if(currentCandidateOffset==8
                    && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                if(this.pieceColor.ispawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,candidateDestinationCoordinate)));
                }
                else {
                    legalMoves.add(new Move.PawnMove(board,this,candidateDestinationCoordinate));
                }
            }
            /*double jump, depending upon the pieceColor and the two forward tiles are empty,
             and it is the first move*/
            else if (currentCandidateOffset==16
                    && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceColor().isBlack())
                            || (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceColor().isWhite()) )) {
                final int behindCandidateDestinationCoordinate=this.piecePosition+(this.pieceColor.getDirection()*8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.PawnJump(board,this,candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset==7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite())
                            || ((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())))) {   //the pawn has some exceptions when in the first column as black and eight column as white,if we have the coordinate offset as 7
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceColor !=pieceOnCandidate.getPieceColor()){
                        if(this.pieceColor.ispawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate)));
                        }
                        else{
                            legalMoves.add(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                }
                else if(board.getEnPassantPawn()!=null){
                    if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceColor.getOppositeDirection()))){
                        final Piece pieceOnCandidate=board.getEnPassantPawn();
                        if(this.pieceColor !=pieceOnCandidate.getPieceColor()){
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                }
            }
            else if(currentCandidateOffset==9 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isBlack())
                    || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isWhite()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceColor !=pieceOnCandidate.getPieceColor()){
                        if(this.pieceColor.ispawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate)));
                        }
                        else{
                            legalMoves.add(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                }
                else if(board.getEnPassantPawn()!=null){
                    if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition-(this.pieceColor.getOppositeDirection()))){
                        final Piece pieceOnCandidate=board.getEnPassantPawn();
                        if(this.pieceColor !=pieceOnCandidate.getPieceColor()){
                            legalMoves.add(new Move.PawnEnPassantAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false);
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition,this.pieceColor,false);
    }
}
