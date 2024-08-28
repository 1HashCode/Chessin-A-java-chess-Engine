package chess.chessEngine.player;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;
import chess.chessEngine.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

//white player class
public class WhitePlayer extends Player{
    public WhitePlayer(Board board, List<Move> whiteStandardLegalMoves, List<Move> blackStandardLegalMoves) {
        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }

    /*getters*/
    @Override
    public List<Piece> getActivePieces(){
        return this.board.getWhitePieces();
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.White;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }


    /**/
    @Override
    protected List<Move> calculateKingCastles(List<Move> playerLegals, List<Move> opponentLegals) {
        final List<Move> kingCastles=new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){  //if the player king is moving the first time and whether the king is not in check
            if(!this.board.getTile(61).isTileOccupied()
                    && !this.board.getTile(62).isTileOccupied()){  //if the middle squares of the king and rook aren't occupied
                final Tile rookTile=this.board.getTile(63); //getting the rookTile
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){ //if the rookTile is occupied and the rook has the first Move
                    if(calculateAttacksOnTile(61,opponentLegals).isEmpty() //if the attacks on the middle tiles are empty
                    && calculateAttacksOnTile(62,opponentLegals).isEmpty()
                    && rookTile.getPiece().getPieceType().isRook()){ //whether it is rook or not

                        /*adding a kingSide castling move*/
                        kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,62,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),61));
                    }
                }
            }

            /*same logic for queenSide castling move*/

            if(!this.board.getTile(59).isTileOccupied()
                    && !this.board.getTile(58).isTileOccupied()
                    && !this.board.getTile(57).isTileOccupied()){
                final Tile rookTile=this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()
                 && Player.calculateAttacksOnTile(58,opponentLegals).isEmpty()
                 && Player.calculateAttacksOnTile(59,opponentLegals).isEmpty()
                 && rookTile.getPiece().getPieceType().isRook()){
                    //TODO
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing,58,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),59));
                }
            }
        }
        return kingCastles;
    }
}
