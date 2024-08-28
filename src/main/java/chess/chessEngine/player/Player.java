package chess.chessEngine.player;

import chess.chessEngine.pieces.King;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final Board board;
    /*Player King*/
    protected final King playerKing;
    /*legalMoves for the player*/
    protected final List<Move>legalMoves;
    /*whether the player is in check*/
    private final boolean isInCheck;

    public Player(final Board board, final List<Move> legalMoves, final List<Move>opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
        final List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(legalMoves);
        allLegalMoves.addAll(calculateKingCastles(legalMoves,opponentMoves));
        this.legalMoves = Collections.unmodifiableList(allLegalMoves);
    }


    /*the moves returned where a move attacks  a piecePosition*/
    protected static List<Move> calculateAttacksOnTile(int piecePosition, List<Move> moves){
        final List<Move>attackMoves=new ArrayList<>();
        for(final Move move: moves){
            if(piecePosition ==move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }


    //getters
    public King getPlayerKing(){return this.playerKing;}

    public List<Move> getLegalMoves() {
        return this.legalMoves;
    }
    public boolean isMoveLegal (final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }


    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves(); //isInCheck and the player has no escape Moves
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves(); //the player is not in check and the king has no escape or any other moves
    }

    public boolean isCastled(){
        return false;
    }


    /*executing the move and seeing that whether
    * the move is legal,in check move or a success move*/
    public MoveTransition makeMove(Move move){
        //checking the move is legal or not
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
        }

        /*executing the move and getting the new board*/
        final Board transitionBoard=move.execute();
        /*checking whether the currentPlayer is being checked after the currentPlayer's move is executed*/
        final List<Move>kingAttacks=Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),transitionBoard.currentPlayer().getLegalMoves());
        /*if the king has attacks after
        * playing the move then return a player check move status*/
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        /*if there are no attacks on the king after the
         * current player has played a move then return a success
         * move status and the move*/
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }


    /*getting the active pieces of the
     * board and the king is searched
     * and returned*/
    private King establishKing(){
        for(final Piece piece:getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King)piece;
            }
        }
        throw new RuntimeException("cannot reach");
    }


    /*to check if any escape move is there or not.
    * getting all the legalMoves and then executing the move
    * and getting the move status and if the moveStatus is Done
    * or success then we say we have escape moves*/
    private boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }


    public abstract List<Piece> getActivePieces();
    public abstract PieceColor getPieceColor();
    public abstract Player getOpponent();

    protected abstract List<Move> calculateKingCastles(List<Move>playerLegals,List<Move>opponentLegals);

}
