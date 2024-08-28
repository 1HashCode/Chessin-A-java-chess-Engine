package chess.chessEngine.board;
import chess.chessEngine.pieces.Piece;

import java.util.*;

public abstract class Tile{  //represents the chess board tiles
    protected final int tileCoordinate;
    /*creating all the possible EmptyTiles in the board as we know
     their state is fixed so their state is fixed*/
    static final Map<Integer,EmptyTile> Empty_Tile_Cache =createAllPossibleEmptyTiles();
    private Tile(int tileCoordinate){
        this.tileCoordinate=tileCoordinate;
    }

    public int getTileCoordinate(){  //getter for the tileCoordinate
        return this.tileCoordinate;
    }

    /*the only function accessible by the users of this class.
    * The createTile method creates an occupied or unoccupied tile then returns them  */
    public static Tile createTile(int coordinate, Piece piece){
        return piece==null? Empty_Tile_Cache.get(coordinate):new OccupiedTile(coordinate,piece);
    }

    /*caching all the empty tiles in a hashMap*/
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile>map=new HashMap<>();
        for(int i=0;i<64;++i){
            map.put(i,new EmptyTile(i));
        }
        return map;
    }

    //abstract methods
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();


    /*static empty tile class extending the abstract tile class*/
    public static final class EmptyTile extends Tile{
        private EmptyTile(final int tileCoordinate){
            super(tileCoordinate);
        }

        @Override
        public String toString(){
            return "-";
        }
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        @Override
        public Piece getPiece() {
            return null;
        }
    }

    /*static occupied tile class extending the abstract tile class*/
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;  //piece on the occupied tile
        private OccupiedTile (int tileCoordinate,Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile=pieceOnTile;
        }

        @Override
        public String toString(){
            return  getPiece().getPieceColor().isBlack()?getPiece().toString().toLowerCase():
                    getPiece().toString();
        }
        @Override
        public boolean isTileOccupied(){
            return true;
        }
        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }

}
