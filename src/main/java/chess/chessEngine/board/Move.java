package chess.chessEngine.board;

import chess.chessEngine.pieces.Pawn;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.pieces.Rook;

public abstract class Move {
    // the working board
    protected final Board board;
    //the movedPiece in the usersMove
    protected final Piece movedPiece;
    //the destination on which the move will land
    protected final int destinationCoordinate;
    protected  boolean isFirstMove;

    /*having a null move as a constant to represent null move*/
    public static final Move NULL_MOVE=new NullMove();

     Move(Board board,Piece movedPiece,int destinationCoordinate){
        this.board=board;
        this.movedPiece=movedPiece;
        this.destinationCoordinate=destinationCoordinate;
        this.isFirstMove=movedPiece.isFirstMove();
    }

    private Move(final Board board,
                 final int destinationCoordinate){
         this.board=board;
         this.destinationCoordinate=destinationCoordinate;
         this.movedPiece=null;
         this.isFirstMove=false;
    }

    //getters
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
         return this.destinationCoordinate;
    }

    public Piece getMovedPiece(){
         return this.movedPiece;
    }

    /*if we execute a move, the existing board doesn't change,
         a new board is created, if the given move was played
         in the old board then the changed board is created and returned. */
    public Board execute() {
        final Board.Builder builder = new Board.Builder(); //building a new board
        /*setting all the existing pieces of the currentPlayer
        except the  piece which is being moved*/
        for(final Piece piece: this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        /*setting all the opponent pieces*/
        for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        /*getting a piece from the movedPiece method
        * from the getters from the move object*/
        builder.setPiece(this.movedPiece.movePiece(this));
        /*setting the player as the opponent's*/
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
        return builder.build(); //building the board
    }

    //hashCode method
    @Override
    public int hashCode(){
        final int prime=31;
        int result=1;

        result=prime*result+this.destinationCoordinate;
        result=prime*result+this.movedPiece.hashCode();
        result=prime*result+this.movedPiece.getPiecePosition();
        return result;
    }

    //equals method
    @Override
    public boolean equals(final Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }

        final Move otherMove=(Move)other;
        return  this.getCurrentCoordinate()==otherMove.getCurrentCoordinate() &&
                this.getDestinationCoordinate()==otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    //getters
    public boolean isAttack(){
        return false;
    }

    public  boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece() {
        return board.getTile(destinationCoordinate).getPiece();
    }
    public Board getBoard(){
        return this.board;
    }

    /*normal MajorMove class extending
    * the move class*/
    public static final class MajorMove extends Move{

        public MajorMove(Board board,Piece movedPiece,int destinationCoordinate){
            super(board,movedPiece,destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class MajorAttackMove extends AttackingMove{
        public MajorAttackMove(final Board board,final Piece pieceMoved,final int destinationCoordinate,final Piece pieceAttacked){
            super(board,pieceMoved,destinationCoordinate,pieceAttacked);
        }

        @Override
        public boolean equals(final Object other){
            return this==other|| other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    /*static class to represent attacking move
    * where the attack method is overridden
    * getting the attacked piece*/
    public static  class AttackingMove extends Move{
        final Piece attackedPiece;
        public AttackingMove(Board board,Piece movedPiece,int destinationCoordinate,Piece attackedPiece){
            super(board,movedPiece,destinationCoordinate);
            this.attackedPiece=attackedPiece;
        }

        public  Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        public int hashCode(){
            return this.attackedPiece.hashCode()+super.hashCode();
        }

        public boolean equals(final Object other){
            if(this==other){
                return true;
            }

            if(!(other instanceof AttackingMove)){
                return false;
            }

            final AttackingMove otherAttackMove=(AttackingMove)other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());

        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /*representing a pawnMove*/
    public static final class PawnMove extends Move{

        public PawnMove(Board board,Piece movedPiece,int destinationCoordinate){
            super(board,movedPiece,destinationCoordinate);
        }

        @Override
        public boolean equals(final Object o){
            return this==o || o instanceof PawnMove && super.equals(o);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /*pawn attack move class */
    public static class PawnAttackMove extends AttackingMove{
        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board,movedPiece,destinationCoordinate,attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0)+"x"+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /*EnPassantMove class*/
    public static final class PawnEnPassantAttackMove extends PawnAttackMove{
        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board,movedPiece,destinationCoordinate,attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute(){
            final Board.Builder builder =new Board.Builder();
            for(final Piece piece:this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            return builder.build();
        }
    }

    /*PawnJump move where the execute move is overridden
    * and has a similar but a new implementation
    * where there is information about the enPassant move*/
    public static final class PawnJump extends Move{

        public PawnJump(Board board,Piece movedPiece,int destinationCoordinate){
            super(board,movedPiece,destinationCoordinate);
        }

        @Override
       public Board execute(){
            final Board.Builder builder=new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn=(Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            return builder.build();
       }

       @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
       }

    }

    public static class PawnPromotion extends Move{
        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());
            this.decoratedMove=decoratedMove;
            this.promotedPawn=(Pawn) decoratedMove.getMovedPiece();
        }
        @Override
        public Board execute(){
            final Board pawnMovedBoard=this.decoratedMove.execute();
            final Board.Builder builder=new Board.Builder();
            for(final Piece piece:pawnMovedBoard.currentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getPieceColor());
            return builder.build();
        }
        @Override
        public int hashCode(){
            return decoratedMove.hashCode()+(31*promotedPawn.hashCode());
        }
        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof PawnPromotion && (super.equals(other));
        }
        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString(){
            return "";
        }

    }


    /*CastleMove extends move*/
    public static abstract class CastleMove extends Move{
        /*about castleRook piece
        * about castleRook starting position
        * about castleRook destination position*/
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination){
            super(board,movedPiece,destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        /*overriding the execute method */
        @Override
        public Board execute(){
            final Board.Builder builder=new Board.Builder();

            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceColor()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime=31;
            int result=super.hashCode();
            result=prime*result+this.castleRook.hashCode();
            result=prime*result+this.castleRookDestination;
            return result;
        }

        public boolean equals(final Object other){
            if(this==other)
                return true;
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove=(CastleMove)other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }


    /*King Side castleMove */
    public static class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(Board board,Piece movedPiece,int destinationCoordinate,Rook castleRook,int castleRookStart,int castleRookDestination){
            super(board,movedPiece,destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }

        @Override
        public String toString(){
            return "0-0";
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof KingSideCastleMove && super.equals(other);
        }

    }


    /*Queen Side castle Move*/
    public static class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(Board board,Piece movedPiece,int destinationCoordinate,Rook castleRook,int castleRookStart,int castleRookDestination){
            super(board,movedPiece,destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }

        @Override
        public String toString(){
            return "0--0";
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }


    /*nullMove*/
    public static class NullMove extends Move{

        public NullMove(){
            super(null,-1);
        }

        public Board execute(){
            throw new RuntimeException("cannot execute the null move!");
        }
    }


    /*To Understand*/
    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("not instantiable");
        }

        public static Move createMove(final Board board,final int currentCoordinate, final int destinationCoordinate){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}

