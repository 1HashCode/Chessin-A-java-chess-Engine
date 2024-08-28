package chess.gui;
import chess.chessEngine.board.Board;
import chess.chessEngine.board.BoardUtils;
import chess.chessEngine.board.Move;
import chess.chessEngine.board.Tile;
import chess.chessEngine.pieces.Piece;
import chess.chessEngine.player.MoveTransition;
import chess.chessEngine.ai.MiniMax;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class MainGUI extends Observable {
    private final RightGameMoveLog rightGameMoveLog;
    private final TakenPiecesGUI takenPiecesGUI;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private final boolean highlightLegalMoves;
    private final static Dimension OUTER_FRAME_DIMENSION=new Dimension(800,700);
    private final static Dimension BOARD_PANEL_DIMENSION=new Dimension(400,400);
    private final static Dimension TILE_PANEL_DIMENSION=new Dimension(50,50);
    private final static Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 8);

    private final static String defaultPieceImagesPath="chessPieceImg/";
    private final Color lightTileColor = Color.decode("#ADD8E6");
    private final Color darkTileColor = Color.decode("#1C3D5A");
    private static final MainGUI MAIN_GUI_INSTANCE =new MainGUI();
    private MainGUI(){
        JFrame gameFrame = new JFrame("JavaChess");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar= createTableMenuBar();
        gameFrame.setJMenuBar(tableMenuBar);
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard=Board.createStandardBoard();
        this.rightGameMoveLog =new RightGameMoveLog();
        this.takenPiecesGUI =new TakenPiecesGUI();
        this.boardPanel=new BoardPanel();
        this.boardDirection=BoardDirection.NORMAL;
        this.moveLog=new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup=new GameSetup(gameFrame,true);
        highlightLegalMoves=true;
        gameFrame.add(this.takenPiecesGUI,BorderLayout.WEST);
        gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        gameFrame.add(this.rightGameMoveLog,BorderLayout.EAST);
        this.getBoardPanel().setBorder(new EtchedBorder(EtchedBorder.RAISED));
        boardPanel.setBorder(border);
        center(gameFrame);
        gameFrame.setVisible(true);
    }


    private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private Board getGameBoard(){
        return this.chessBoard;
    }

    public static MainGUI getMainInstance(){
        return MAIN_GUI_INSTANCE;
    }
    public void show() throws Exception {
        MainGUI.getMainInstance().getMoveLog().clear();
        MainGUI.getMainInstance().getRightGameMoveLog().redo(chessBoard, MainGUI.getMainInstance().getMoveLog());
        MainGUI.getMainInstance().getTakenPiecesGUI().redo(MainGUI.getMainInstance().getMoveLog());
        MainGUI.getMainInstance().getBoardPanel().drawBoard(MainGUI.getMainInstance().getGameBoard());
    }
    private GameSetup getGameSetup(){
        return this.gameSetup;
    }

    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }


    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu=new JMenu("Flip Board");
        final JMenuItem flipBoardMenuItem=new JMenuItem("Flip Board");

        flipBoardMenuItem.addActionListener(e -> {
            boardDirection=boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){
        final JMenu optionsMenu=new JMenu("GameSetup");
        final JMenuItem setupGameMenuItem=new JMenuItem("Setup Game");

        setupGameMenuItem.addActionListener(e -> {
            MainGUI.getMainInstance().getGameSetup().promptUser();
            MainGUI.getMainInstance().setupUpdate(MainGUI.getMainInstance().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    private void setupUpdate(final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if(MainGUI.getMainInstance().getGameSetup().isAIPlayer(MainGUI.getMainInstance().getGameBoard().currentPlayer()) &&
                    !MainGUI.getMainInstance().getGameBoard().currentPlayer().isInCheckMate() &&
                    !MainGUI.getMainInstance().getGameBoard().currentPlayer().isInStaleMate()){
                    final AIThinkTank thinkTank=new AIThinkTank();
                    thinkTank.execute();
            }
            Board board= MainGUI.getMainInstance().getGameBoard();
            EndGamePanel.checkForGameEnding(board.currentPlayer(), board.currentPlayer().getOpponent());
        }
    }

    public void updateGameBoard(final Board board){
        this.chessBoard=board;
    }


    private MoveLog getMoveLog(){
        return this.moveLog;
    }

    private RightGameMoveLog getRightGameMoveLog(){
        return this.rightGameMoveLog;
    }

    private TakenPiecesGUI getTakenPiecesGUI(){
        return this.takenPiecesGUI;
    }

    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }

    private void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }

    private static class AIThinkTank extends SwingWorker<Move,String>{
        private AIThinkTank(){

        }

        @Override
        public void done(){
            try {
                final Move bestMove=get();
                MainGUI.getMainInstance().updateGameBoard(MainGUI.getMainInstance().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                MainGUI.getMainInstance().getMoveLog().addMove(bestMove);
                MainGUI.getMainInstance().getRightGameMoveLog().redo(MainGUI.getMainInstance().getGameBoard(), MainGUI.getMainInstance().getMoveLog());
                MainGUI.getMainInstance().getTakenPiecesGUI().redo(MainGUI.getMainInstance().getMoveLog());
                MainGUI.getMainInstance().getBoardPanel().drawBoard(MainGUI.getMainInstance().getGameBoard());
                MainGUI.getMainInstance().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected Move doInBackground() {
            int searchDepth= MainGUI.getMainInstance().gameSetup.getSearchDepth();
            final MiniMax miniMax=new MiniMax(searchDepth);
            return miniMax.execute(MainGUI.getMainInstance().getGameBoard());
        }
    }

    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                List<TilePanel>list=new ArrayList<>();
                list.addAll(boardTiles);
                Collections.reverse(list);
                return list;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel>traverse (final List<TilePanel>boardTiles);
        abstract  BoardDirection opposite();
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles=new ArrayList<>();
            for(int i=0;i<64;i++){
                final TilePanel tilePanel=new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel boardTile: boardDirection.traverse(boardTiles)){
                boardTile.drawTile(board);
                add(boardTile);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog{
        private final List<Move>moves;
        MoveLog(){
            this.moves=new ArrayList<>();
        }
        public List<Move>getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
    }


    enum PlayerType{
        HUMAN,
        COMPUTER
    }

    private class TilePanel extends JPanel{
        private final int  tileId;
        TilePanel(final BoardPanel boardPanel,final int tileId){
            super(new GridLayout());
            this.tileId=tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceTile=null;
                        destinationTile = null;
                        humanMovedPiece=null;
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceTile==null){
                            //first click
                            sourceTile=chessBoard.getTile(tileId);
                            humanMovedPiece=sourceTile.getPiece();
                            if(humanMovedPiece==null){
                                sourceTile=null;
                            }
                        }
                        else{
                            //second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move=Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),destinationTile.getTileCoordinate());
                            if(move.getDestinationCoordinate()==-1){
                                sourceTile=null;
                                humanMovedPiece=null;
                                destinationTile=null;
                            }
                            else{
                                final MoveTransition transition=chessBoard.currentPlayer().makeMove(move);
                                if(transition.getMoveStatus().isDone()){
                                    chessBoard=transition.getTransitionBoard();
                                    EndGamePanel.checkForGameEnding(chessBoard.currentPlayer(),chessBoard.currentPlayer().getOpponent());
                                    moveLog.addMove(move);
                                }

                                sourceTile=null;
                                humanMovedPiece=null;
                                destinationTile=null;
                            }
                        }
                    }
                    SwingUtilities.invokeLater(() ->{
                        rightGameMoveLog.redo(chessBoard,moveLog);
                        try {
                            takenPiecesGUI.redo(moveLog);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
                            MainGUI.getMainInstance().moveMadeUpdate(PlayerType.HUMAN);
                        }
                        boardPanel.drawBoard(chessBoard);
                    });
                }


                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceColor().toString().charAt(0)
                    + board.getTile(this.tileId).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }

        private void highlightLegals(final Board board){
            if(highlightLegalMoves){
                for(final Move move:pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate()==this.tileId){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("chessPieceImg/legalMovePointer.png")))));
                        }
                        catch(Exception e){
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }

        private List<Move>pieceLegalMoves(final Board board){
            if(humanMovedPiece!=null && humanMovedPiece.getPieceColor()==board.currentPlayer().getPieceColor()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return new ArrayList<>();
        }

        private void assignTileColor() {
            if(BoardUtils.EIGHT_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] || BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0? lightTileColor : darkTileColor);
            }
            else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId] || BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId %2!=0 ? lightTileColor: darkTileColor);
            }
        }
    }

}
