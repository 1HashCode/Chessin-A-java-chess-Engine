package chess.gui;

import chess.chessEngine.player.Player;

import javax.swing.*;

public class EndGamePanel {

    public static void checkForGameEnding(Player currentPlayer,Player opponentPlayer) {
        if (currentPlayer.isInCheckMate()) {
            showGameOverDialog(opponentPlayer.getPieceColor() + " wins! and "+currentPlayer.getPieceColor()+" is in checkMate");
        }
        else if (currentPlayer.isInStaleMate()) {
            showGameOverDialog("The game is a draw");
        }
    }

    private static void showGameOverDialog(String message) {
        // This will create a dialog box with the game result
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
