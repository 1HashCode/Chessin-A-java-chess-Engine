package chess.chessEngine.board;

import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.pieces.*;
import chess.chessEngine.player.BlackPlayer;
import chess.chessEngine.player.Player;
import chess.chessEngine.player.WhitePlayer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    /*the main 64 squares which represent the main
    * chessboard which is composed of the
    * empty and occupied tiles*/
    private final List<Tile>gameBoard;

    /*the active whitePieces on the board*/
    private final List<Piece> whitePieces;

    /*the active blackPieces on the board*/
    private final List<Piece> blackPieces;

    /*the whitePlayer*/
    private final WhitePlayer whitePlayer;

    /*the blackPlayer*/
    private final BlackPlayer blackPlayer;

    /*the currentPlayer who's turn it is*/
    private final Player currentPlayer;
    private final Pawn enPassantPawn;

    /*private constructor which is called
    * by the static class builder for the
    * chessboard creation */
    private Board(Builder builder){
        /*the builders contents are being copied from
        * the builder to the game board*/

        /*the function creates a list of empty and occupied tiles
         from the hashMap of pieces
        * on the coordinate*/
        this.gameBoard=createGameBoard(builder);
        this.enPassantPawn=builder.enPassantPawn;

        /*getting the current pieces on the board
        * of the respective pieceColor*/
        this.whitePieces = calculateActivePieces(this.gameBoard, PieceColor.White);
        this.blackPieces = calculateActivePieces(this.gameBoard, PieceColor.Black);

        /*Calculating all the legal moves for all the available
        * active pieces according to the respective pieceColor*/
        final List<Move>whiteStandardLegalMoves=calculateLegalMoves(this.whitePieces);
        final List<Move>blackStandardLegalMoves=calculateLegalMoves(this.blackPieces);

        //creating the players for the board
        this.whitePlayer=new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer=new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);

        //setting the currentPlayer by accessing the nextMoveMaker
        /*where the nextMoveMaker will be set by the user
        * where the choosePlayer will choose the player acc to the
        * given nextMoveMaker*/
        this.currentPlayer= builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    //combining all the legalMoves of the white and black pieces
    public Iterable<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return allLegalMoves;
    }

    //the builder class
    public static class Builder{
        Map<Integer, Piece>boardConfig;
        PieceColor nextMoveMaker;

        Pawn enPassantPawn;

        public Builder (){
            this.boardConfig=new HashMap<>();
        }

        public Builder setPiece(final Piece piece){  //set a piece
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        public Builder setMoveMaker(final PieceColor nextMoveMaker){  //setting the move maker
            this.nextMoveMaker=nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }


        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }
    }

    //toString method to print the board
    @Override
    public String toString(){
        final StringBuilder Stbuilder=new StringBuilder();
        for(int i=0;i<64;++i){
            final String tileText=this.gameBoard.get(i).toString();
            Stbuilder.append(String.format("%3s",tileText));
            if((i+1)%8==0){
                Stbuilder.append("\n");
            }
        }
        return Stbuilder.toString();
    }

    //getters

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Tile getTile(int candidateDestinationCoordinate) {
        return gameBoard.get(candidateDestinationCoordinate);
    }

    public List<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public List<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    /*this is the standard board that is set at the
    * beginning of the game, here we set the pieces in the
    * hashMap*/
    public static Board createStandardBoard(){
        final Builder builder = new Builder();

        //black layout
        builder.setPiece(new Rook(0, PieceColor.Black));
        builder.setPiece(new Knight(1, PieceColor.Black));
        builder.setPiece(new Bishop(2, PieceColor.Black));
        builder.setPiece(new Queen(3, PieceColor.Black));
        builder.setPiece(new King(4, PieceColor.Black));
        builder.setPiece(new Bishop(5, PieceColor.Black));
        builder.setPiece(new Knight(6, PieceColor.Black));
        builder.setPiece(new Rook(7, PieceColor.Black));
        builder.setPiece(new Pawn(8, PieceColor.Black));
        builder.setPiece(new Pawn(9, PieceColor.Black));
        builder.setPiece(new Pawn(10, PieceColor.Black));
        builder.setPiece(new Pawn(11, PieceColor.Black));
        builder.setPiece(new Pawn(12, PieceColor.Black));
        builder.setPiece(new Pawn(13, PieceColor.Black));
        builder.setPiece(new Pawn(14, PieceColor.Black));
        builder.setPiece(new Pawn(15, PieceColor.Black));

        // White Layout
        builder.setPiece(new Rook(56, PieceColor.White));
        builder.setPiece(new Knight(57, PieceColor.White));
        builder.setPiece(new Bishop(58, PieceColor.White));
        builder.setPiece(new Queen(59, PieceColor.White));
        builder.setPiece(new King(60, PieceColor.White));
        builder.setPiece(new Bishop(61, PieceColor.White));
        builder.setPiece(new Knight(62, PieceColor.White));
        builder.setPiece(new Rook(63, PieceColor.White));
        builder.setPiece(new Pawn(48, PieceColor.White));
        builder.setPiece(new Pawn(49, PieceColor.White));
        builder.setPiece(new Pawn(50, PieceColor.White));
        builder.setPiece(new Pawn(51, PieceColor.White));
        builder.setPiece(new Pawn(52, PieceColor.White));
        builder.setPiece(new Pawn(53, PieceColor.White));
        builder.setPiece(new Pawn(54, PieceColor.White));
        builder.setPiece(new Pawn(55, PieceColor.White));
        builder.setMoveMaker(PieceColor.White);
        return builder.build();
    }


    /*getting all the pieces from the board
     * of a given pieceColor*/
    private static List<Piece> calculateActivePieces(List<Tile> gameBoard, PieceColor pieceColor) {
        List<Piece>activePieces=new ArrayList<>();
        for(final Tile tile:gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece=tile.getPiece();
                if(piece.getPieceColor()==pieceColor){
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    /*calculating legalMoves available
     * for the current pieces of the list
     * of pieces.*/
    private List<Move> calculateLegalMoves(List<Piece> pieces) {
        final List<Move>legalMoves=new ArrayList<>();
        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    /*converting the map with the pieces to the tiles of the gameBoard*/
    private static List<Tile>createGameBoard(final Builder builder){
        final List<Tile> tiles=new ArrayList<>();
        for(int i=0;i<64;++i){
            tiles.add(Tile.createTile(i,builder.boardConfig.get(i)));
        }
        return tiles;
    }
}
