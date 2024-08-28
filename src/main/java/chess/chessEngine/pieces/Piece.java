package chess.chessEngine.pieces;

import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;
import java.util.*;

public abstract class Piece {
    protected final PieceType pieceType; //enum for pieces
    protected final int piecePosition;
    protected final PieceColor pieceColor;
    //whether it is the first move or not for the piece;
    protected final boolean isFirstMove;

    //caching the hashCode upon object creation as the pieces are immutable
    private final int cachedHashCode;

    protected Piece(final PieceType pieceType, PieceColor pieceColor, int piecePosition, boolean isFirstMove){
        this.pieceType=pieceType;
        this.isFirstMove=isFirstMove;
        this.pieceColor =pieceColor;
        this.piecePosition=piecePosition;
        this.cachedHashCode=computeHashCode();
    }

    /*overriding the equals method for the pieces
    on the object class to compare two piece object*/
    @Override
    public boolean equals(final Object other){
        if(this==other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece=(Piece)other;
        return this.piecePosition==otherPiece.getPiecePosition() && this.pieceType==otherPiece.getPieceType() &&
                pieceColor ==otherPiece.getPieceColor() && isFirstMove==otherPiece.isFirstMove();
    }

    /*hashCode computation for equals method comparison
     for uses in hashMap or hashset*/
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    //getters
    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public PieceColor getPieceColor(){
        return this.pieceColor;
    }

    private int computeHashCode(){
        int result=pieceType.hashCode();
        result=31*result+ pieceColor.hashCode();
        result=31*result+piecePosition;
        result=31*result+(isFirstMove? 1:0);
        return result;
    }

    //abstract method for calculating all the legal moves for a piece;
    public abstract List<Move>calculateLegalMoves(final Board board);

    /*return a piece where the piece is placed
    * at the destination tile of the move object*/
    public abstract Piece movePiece(Move move);

    public int getPieceValue() {
        return this.pieceType.pieceValue();
    }

    public enum PieceType{
        PAWN("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q",900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K",999999) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        private final int pieceValue;
        //constructor for the enums
        PieceType(final String pieceName,int pieceValue){
            this.pieceName=pieceName;
            this.pieceValue=pieceValue;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public int pieceValue(){
            return this.pieceValue;
        }

        //king or not
        public abstract boolean isKing();
        // rook or not
        public abstract boolean isRook();
    }

}
