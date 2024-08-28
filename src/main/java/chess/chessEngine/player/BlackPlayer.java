package chess.chessEngine.player;

import chess.chessEngine.board.Tile;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.pieceColor.PieceColor;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;
import chess.chessEngine.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

// same logic for king castles and other as White player except the castle coordinates
public class BlackPlayer extends Player{
    public BlackPlayer(Board board, List<Move> whiteStandardLegalMoves, List<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    public List<Piece> getActivePieces(){
        return this.board.getBlackPieces();
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.Black;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected List<Move> calculateKingCastles(List<Move> playerLegals, List<Move> opponentLegals) {
        final List<Move> kingCastles=new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(5).isTileOccupied()
                    && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile=this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnTile(5,opponentLegals).isEmpty()
                            && Player.calculateAttacksOnTile(6,opponentLegals).isEmpty()
                            && rookTile.getPiece().getPieceType().isRook()){
                        //TODO
                        kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,6,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),5));
                    }
                }
            }

            if(!this.board.getTile(1).isTileOccupied()
                    && !this.board.getTile(2).isTileOccupied()
                    && !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile=this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()
                && Player.calculateAttacksOnTile(2,opponentLegals).isEmpty()
                && Player.calculateAttacksOnTile(3,opponentLegals).isEmpty()
                &&  rookTile.getPiece().getPieceType().isRook()){
                    //TODO
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing,2,(Rook)rookTile.getPiece(),rookTile.getTileCoordinate(),3));
                }
            }
        }
        return kingCastles;
    }
}
