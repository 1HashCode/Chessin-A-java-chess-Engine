package chess.chessEngine.player;

//moveStatus enum representing a DONE move,Illegal and playerInCheck move;
public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone(){
            return false;
        }
    }, LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
