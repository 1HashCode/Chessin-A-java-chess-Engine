package chess.chessEngine.pieceColor;

import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.player.BlackPlayer;
import chess.chessEngine.player.Player;
import chess.chessEngine.player.WhitePlayer;

/*the enum which represents the black and white
* colors in the chess and the methods associated with it*/

public enum PieceColor {
    Black{
        /*the direction is positive as
        * the pawn moves in the forward position
        * in the board arrangement*/
        public int getDirection(){
            return 1;
        }

        public boolean isWhite(){
            return false;
        }

        public boolean isBlack(){
            return true;
        }

        /*choosing the player according to the enum*/
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public boolean ispawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

    },
    White{
        /*the direction is negative as
         * the pawn moves in the forward position
         * in the board arrangement*/
        public int getDirection(){
            return -1;
        }

        public boolean isWhite(){
            return true;
        }

        public boolean isBlack(){
            return false;
        }

        /*choosing the player according to the enum*/
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public boolean ispawnPromotionSquare(int position) {
            return BoardUtils.EIGHT_RANK[position];
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }
    };
    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract int getOppositeDirection();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    public abstract boolean ispawnPromotionSquare(int position);
    }

