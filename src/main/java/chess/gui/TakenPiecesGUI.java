package chess.gui;

import chess.chessEngine.board.Move;
import chess.chessEngine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakenPiecesGUI extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final EtchedBorder PANEL_BORDER=new EtchedBorder(EtchedBorder.RAISED);
    private static final Dimension TAKEN_PIECES_PANEL_DIMENSION =new Dimension(90,180);
    private static final Color PANEL_COLOR=Color.decode("0xFDF5E6");

    public TakenPiecesGUI(){
        super(new BorderLayout());
        setBackground(Color.decode("0xFDF5E6"));
        setBorder(PANEL_BORDER);
        this.northPanel=new JPanel(new GridLayout(8,2));
        this.southPanel=new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        add(this.northPanel,BorderLayout.NORTH);
        add(this.southPanel,BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
    }

    public void redo (final MainGUI.MoveLog movelog) throws Exception {
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece>whiteTakenPieces=new ArrayList<>();
        final List<Piece>blackTakenPieces=new ArrayList<>();

        for(final Move move:movelog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece=move.getAttackedPiece();
                if(takenPiece.getPieceColor().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }
                else if(takenPiece.getPieceColor().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else{
                    throw new RuntimeException("should not reach here");
                }
            }
        }
        whiteTakenPieces.sort(Comparator.comparingInt(Piece::getPieceValue));
        blackTakenPieces.sort(Comparator.comparingInt(Piece::getPieceValue));

        for(final Piece takenPiece:whiteTakenPieces){
            try{
                final BufferedImage image= ImageIO.read(new File("chessPieceImg/"+ takenPiece.getPieceColor().toString().charAt(0)+takenPiece.toString()+".png"));
                final ImageIcon icon=new ImageIcon(image);
                final JLabel imageLabel=new JLabel(icon);
                this.southPanel.add(imageLabel);
            }
            catch(final IOException e){
                throw new Exception();
            }
        }

        for(final Piece takenPiece:blackTakenPieces){
            try{
                final BufferedImage image= ImageIO.read(new File("chessPieceImg/"+ takenPiece.getPieceColor().toString().charAt(0)+takenPiece.toString()+".png"));
                final ImageIcon icon=new ImageIcon(image);
                final JLabel imageLabel=new JLabel(icon);
                this.northPanel.add(imageLabel);
            }
            catch(final IOException e){
                throw new Exception();
            }
        }
        validate();
    }


}
