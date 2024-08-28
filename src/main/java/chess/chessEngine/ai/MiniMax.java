package chess.chessEngine.ai;

import chess.chessEngine.board.Board;
import chess.chessEngine.board.Move;
import chess.chessEngine.player.MoveTransition;

public class MiniMax{
    private final staticPositionEvaluator boardEvaluator;
    private final int searchDepth;
    public MiniMax(int searchDepth){
        this.boardEvaluator=new staticPositionEvaluator();
        this.searchDepth=searchDepth;
    }

    public Move execute(Board board) {
        int highestSeenValue=Integer.MIN_VALUE;
        int lowestSeenValue=Integer.MAX_VALUE;
        Move bestMove=null;
        int currentValue;
        System.out.println(board.currentPlayer()+"Thinking with depth"+this.searchDepth);

        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition=board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                currentValue=board.currentPlayer().getPieceColor().isWhite()?
                        min(moveTransition.getTransitionBoard(),Integer.MIN_VALUE,Integer.MAX_VALUE,this.searchDepth-1):
                        max(moveTransition.getTransitionBoard(),Integer.MIN_VALUE,Integer.MAX_VALUE,this.searchDepth-1);
                if(board.currentPlayer().getPieceColor().isWhite() && currentValue>=highestSeenValue){
                    highestSeenValue=currentValue;
                    bestMove=move;
                }
                else if(board.currentPlayer().getPieceColor().isBlack() && currentValue<=lowestSeenValue){
                    lowestSeenValue=currentValue;
                    bestMove=move;
                }
            }
        }
        return bestMove;
    }

    public int min(final Board board,int alpha,int beta,final int depth){
        if(depth==0){
            return this.boardEvaluator.evaluate(board,depth);
        }
        int lowestSeenValue=Integer.MAX_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition=board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue=max(moveTransition.getTransitionBoard(),alpha,beta,depth-1);
                lowestSeenValue=Math.min(currentValue,lowestSeenValue);
                beta=Math.min(currentValue,beta);
                if(beta<=alpha){  //alpha beta pruning
                    break;
                }
            }
        }
        return lowestSeenValue;
    }
    public int max(final Board board,int alpha,int beta,final int depth){
        if(depth==0){
            return this.boardEvaluator.evaluate(board,depth);
        }
        int highestSeenValue=Integer.MIN_VALUE;
        for(final Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition=board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue=min(moveTransition.getTransitionBoard(),alpha,beta,depth-1);
                highestSeenValue=Math.max(currentValue,highestSeenValue);
                alpha=Math.max(alpha,currentValue);
                if(beta<=alpha){
                    break;
                }
            }
        }
        return highestSeenValue;
    }

    @Override
    public String toString(){
        return "MiniMax";
    }
}
