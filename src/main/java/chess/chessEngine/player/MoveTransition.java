package chess.chessEngine.player;

import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;

/*a move transition class to represent
* the moves being played and then
* checking the validity of the moves*/
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board trannsitionBoard, final Move move, final MoveStatus moveStatus){
        this.moveStatus=moveStatus;
        this.move=move;
        this.transitionBoard=trannsitionBoard;
    }

    //the moveStatus getter
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }

}
