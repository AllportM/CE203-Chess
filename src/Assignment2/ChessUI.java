package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import static Assignment2.Team.*;

/*
    ChessUI - The purpose of this class is to initiate the main UI for the chess game. Member variables
    are instantiated for access throughout other classes, default values such as size and title are initializes,
    and a player welcome screen is created. From the welcome screen, and each time a user selects new game from the
    menu, a chess panel is instantiated via init chess, which controls the gameplay thereafter.
    A method for creating the end game winner panel is also contained within, and is triggered at the end
    of each game.

    Initially a welcome screen is created, from that when the user clicks start game initChess is called to
    instantiate the main chess panel, and create a 'Board' in which pertains the players, pieces, and tiles in order
    to play the game. A keyboard listener 'MyKeyListener' is attached to the main chess panel, and mouse listeners
    'TileHandler' is attached to each tile, which extends JPanel, to react to mouse events.

    Upon game ending, which is determined through the Board class, initWinner is called which instantiates
    'WinPanel', which extends JPanel, to open scores and display them

    The main elements involves in ui are ChessUI, PlayerWelcome, Tile, and WinPanel in which the last three extend
    JPanel. I have kept these in one .java file for easier reading, whereas the keyboardlistener and mouselistener are
    seperated, both named 'MyKeyListener' and 'TileHandler' respectively.
 */
public class ChessUI extends JFrame{
    Board chessBoard; // variable with reference to the board object, used for handlers to access the board and its variables
    JButton startGame; // start game button, accessed through handlers to control initChess on button click
    ButtonHandler buttonH; // main button handler, used for JPanels to attach the same button listener
    private MenuBar menu; // the menu variable, used within the button handler class to identify menu items clicked
    private JPanel chessPanel; // the main chess parent panel, used via initWinner and reDoChess for access
    MyKeyListener keyListener; // the keyboard listener, used within tileHandler mouse listener to reset variables
    private JPanel welcome;

    /*
     * Default non argument constructor, instantiates member variables, sets defaults, and calls the initial welcome
     * screen
     */
    private ChessUI()
    {
        setTitle("Ma18533 / 1802882 - Chess Engine by Mal");
        setFocusable(true);
        buttonH = new ButtonHandler(this);
        startGame = new JButton("I want to play a game...");
        menu = new MenuBar(this);
        setPreferredSize(new Dimension(800, 800));
        initPlayerWelcome();
        setJMenuBar(menu);
    }

    /*
     * initPlayerWelcome's main purpose is to instantiate the welcome screen class 'PlayerWelcome'
     */
    void initPlayerWelcome()
    {
        if (getContentPane() != null) getContentPane().removeAll();
        chessPanel = null;
        setLayout(new BorderLayout());
        setJMenuBar(menu);
        welcome = new PlayerWelcome(this);
        add(welcome, BorderLayout.CENTER);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
    }

    /*
     * initChess's purpose is to initialize the chess panel, containing an array of 'Tile''s, and instantiate
     * the Board class, whilst attaching listeners and setting default values'
     *
     * @params
     *      teamCol, a string identifying the colour a player has chosen
     *      op, a string identifying the type of opponent to play against, human or ai
     *      name, a string identifying the main players name
     *
     * @return - none
     */
    void initChess(String teamCol, String op, String name)
    {
        getContentPane().removeAll();
        setJMenuBar(menu);
        chessBoard = new Board(this, teamCol, op, name);
        chessPanel = new JPanel();
        chessPanel.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < chessBoard.board.length; i++)
        {
            for (Tile tile: chessBoard.board[i])
            {
                chessPanel.add(tile);
            }
        }
        add(chessPanel);
        keyListener = new MyKeyListener(chessBoard);
        addKeyListener(keyListener);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
        repaint();
    }

    /*
     * initWinner's main purpose is to instantiate a WinPanel at the end of a game displaying users scores
     * aswell as the top 5 scores of all time, whilst highling the row if the current score has achieved top 5
     * in purple
     */
    void initWinner()
    {
        // slight delay so that user can see where they were taken
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ignore){}
        getContentPane().removeAll();
        JPanel overlayPan = new JPanel();
        overlayPan.setLayout(new OverlayLayout(overlayPan));
        if (!(chessPanel == null))
        {
        overlayPan.add(new WinPanel(chessBoard));
        overlayPan.add(chessPanel);
        add(overlayPan);
        setVisible(true);
        pack();
        repaint();
    }

    /*
     * reDoChess's main purpose is to show the chess board again once a game is over and the user clicks close
     * to remove the top scores and view the board again
     */
    void reDoChess()
    {
        getContentPane().removeAll();
        add(chessPanel);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
        repaint();
    }

    // main function
    public static void main(String[] args) {
        ChessUI current = new ChessUI();
        current.repaint();
    }

    // returns private menu variable
    MenuBar getMenu() {
        return menu;
    }
}

/*
 * Tile's purpose is to create a JPanel container for each individual board tile. The class has variables
 * pertaining to the different colours it can have, a member variable for the piece placed within it.
 *
 * The tiles background/shape changes dynamically throughout the game, with keyboardlistener, mouselistener, and board
 * controlling the tiles colours in accordance to pieces players can move and moves available
 */
class Tile extends JPanel {
    private Piece tilePiece; // a reference to the active piece placed on this tile
    private Rectangle background; // the background shape for a tile, see Shape.java
    private Color toPaint; // the active colour for the tiles background
    private final Color hostileCol = new Color(201, 36, 36); // the colour for offensive moves
    private final Color movingColour = new Color(50, 166, 108); // the colour indicating a player can move a piece
    private final Color movePieceColour = new Color(127, 67, 196);
    private final Color defaultCol;
    private final Coord tileCoord;
    private final int width = 100;
    private final int height = 100;

    Tile(Coord tilePlace)
    {
        tileCoord = tilePlace;
        if (tilePlace.y%2 == 1)
        {
            if (tilePlace.x%2 == 1) defaultCol = new Color(205,133,63);
            else defaultCol = new Color(255, 222, 173);
        }
        else
        {
            if (tilePlace.x%2 == 1) defaultCol = new Color(255, 222, 173);
            else defaultCol = new Color(205,133,63);
        }
        toPaint = defaultCol;
        background = new Rectangle(0, width, 0, height, toPaint);
        setPreferredSize(new Dimension(width, height));
    }


    @Override
    public void paintComponent(Graphics g)
    {
        background.renderShape(g);
        if (tilePiece != null) tilePiece.drawing.renderShape(g);
    }

    @Override
    public String toString()
    {
        return "I am a Tile, my piece is:- \n" + getTilePiece() +"\nMy posittion is:- \n" + getTileCoord();
    }

    void setColourMovePiece()
    {
        toPaint = movePieceColour;
        background = new Rectangle(0, width, 0, height, toPaint);
    }

    void setColourHostile()
    {
        toPaint = hostileCol;
        background =  new Rectangle(0, width, 0, height, toPaint);
    }

    void setColourMoving()
    {
        toPaint = movingColour;
        background =  new Rectangle(0, width, 0, height, toPaint);
    }

    void setColourDefault()
    {
        toPaint = defaultCol;
        background =  new Rectangle(0, width, 0, height, toPaint);
    }

    void setPiece(Piece piece)
    {
        if (this.isOccupied())
        {
            clearPiece();
        }
        tilePiece = piece;
        piece.setCoord(tileCoord);
    }

    void clearPiece()
    {
        tilePiece = null;
    }

    boolean isOccupied()
    {
        return tilePiece != null;
    }

    Coord getTileCoord()
    {
        return tileCoord;
    }

    Piece getTilePiece()
    {
        return tilePiece;
    }
}

class PlayerWelcome extends JPanel {
    ButtonGroup teamChoices; // used to contain radio buttons for team colour identification in button handler
    ButtonGroup oppChoices; // used to containg radio buttons for opponent choices in button handler
    JTextField playerName; // used to contain players name for button handler

    PlayerWelcome(ChessUI ui) {
        teamChoices = new ButtonGroup();
        oppChoices = new ButtonGroup();
        playerName = new JTextField(10);
        setPreferredSize(new Dimension(800, 800));
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        ComplexGBL gblGroup = new ComplexGBL();
        JLabel welcome = new JLabel("Welcome! Lets play some chess..");
        gblGroup.addToList(new GBLLeaf(1, 1, 4, 1,
                new Insets(0, 0, 100, 0), welcome));

        JLabel teamChoice = new JLabel("White or Black:");
        gblGroup.addToList(new GBLLeaf(1, 2, 1, 2,
                new Insets(0, 0, 0, 28), teamChoice));
        JRadioButton white = new JRadioButton("White");
        white.setActionCommand("white");
        white.setSelected(true);
        gblGroup.addToList(new GBLLeaf(2, 2, 1, 1,
                new Insets(0, 10, 0, 0), white));
        JRadioButton black = new JRadioButton("Black");
        black.setActionCommand("black");
        gblGroup.addToList(new GBLLeaf(2, 3, 1, 1,
                new Insets(0, 10, 0, 0), black));
        teamChoices.add(white);
        teamChoices.add(black);

        JLabel oppositionChoice = new JLabel("Human or Ai opponent:");
        gblGroup.addToList(new GBLLeaf(3, 2, 1, 2,
                new Insets(0, 100, 0, 0), oppositionChoice));
        JRadioButton human = new JRadioButton("Human");
        human.setActionCommand("human");
        gblGroup.addToList(new GBLLeaf(4, 2, 1, 1,
                new Insets(0, 28, 0, 0), human));
        JRadioButton ai = new JRadioButton("AI");
        ai.setActionCommand("ai");
        ai.setSelected(true);
        gblGroup.addToList(new GBLLeaf(4, 3, 1, 1, ai));
        oppChoices.add(ai);
        oppChoices.add(human);

        JLabel pName = new JLabel("Enter your name: ");
        gblGroup.addToList(new GBLLeaf(2, 4, 1, 1,
                new Insets(50, 0, 10, 0), pName));
        gblGroup.addToList(new GBLLeaf(3, 4, 1, 1,
                new Insets(50, 10,10,0), playerName));

        JButton play = ui.startGame;
        play.addActionListener(ui.buttonH);
        gblGroup.addToList(new GBLLeaf(1, 5, 4, 1,
                new Insets(100, 0, 0, 0), play));
        gblGroup.addGBL(gbc, gbl, this);

        JPanel vs = new JPanel();
        vs.setLayout(new GridLayout(1, 3));
        Tile whiteSample = new Tile(new Coord(0, 0));
        whiteSample.setPiece(getPiece( 1));
        Tile blackSample = new Tile(new Coord(2, 0));
        blackSample.setPiece(getPiece( 2));
        Tile blankSample = new Tile(new Coord(1, 0));
        JLabel vsLabel = new JLabel("VS");
        blankSample.setBorder(BorderFactory.createEmptyBorder(35,0,40,0));
        blankSample.add(vsLabel);
        vs.add(whiteSample);
        vs.add(blankSample);
        vs.add(blackSample);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0,0,100,0);
        gbl.setConstraints(vs, gbc);
        add(vs);
    }

    static Piece getPiece(int team)
    {
        int randomPiece = (int) (Math.random() * 6);
        Team col = (team == 1)? Team.WHITE: Team.BLACK;
        switch(randomPiece)
        {
            case 0:
                return new Pawn(new Coord(0, 0), P1, col);
            case 1:
                return new Castle(new Coord(0, 0), P1, col);
            case 2:
                return new Horse(new Coord(0, 0), P1, col);
            case 3:
                return new Bishop(new Coord(0, 0), P1, col);
            case 4:
                return new Queen(new Coord(0, 0), P1, col);
            case 5:
                return new King(new Coord(0, 0), P1, col);
        }
        return null;
    }

}

class WinPanel extends JPanel {
    private Tile tile = new Tile(new Coord(0,0));

    WinPanel(Board board)
    {
        ScoreHandler scores = new ScoreHandler("scores.txt");
        setPreferredSize(new Dimension(800, 800));
        GridBagLayout gblInner = new GridBagLayout();
        GridBagConstraints gbcInner = new GridBagConstraints();
        ComplexGBL gblGroupInner = new ComplexGBL();
        JPanel inner = new JPanel();
        inner.setPreferredSize(new Dimension(400, 500));
        inner.setLayout(gblInner);

        GridBagLayout gblPrimary = new GridBagLayout();
        GridBagConstraints gbcPrimary = new GridBagConstraints();
        ComplexGBL gblGroupPrimary = new ComplexGBL();
        gblGroupPrimary.addToList(new GBLLeaf(0,1,inner));
        setLayout(gblPrimary);
        JLabel name, time, score;
        PlayerScore playerScore = null;

        if (board.isGameOver())
        {
            // generates a random chess piece from either the winners team, or player 1's team if stalemate
            int winner;
            if (board.getWinner() != null)
            {
                winner = (board.getWinner().getColour() == Team.WHITE)? 1: 0;
            }
            else winner = (board.getP1().getColour() == Team.WHITE)? 1: 0;
            Piece winnerPiece = PlayerWelcome.getPiece(winner);
            // creates tile for piece and sets constraints
            Tile tile = new Tile(new Coord(0,0));
            tile.setPiece(winnerPiece);
            add(tile);
            gblGroupInner.addToList(new GBLLeaf(0, 0, 2, 2, new Insets(0, 50, 50, 0), tile));

            // adds gameover label
            String gOver = (((HumanPlayer) board.getP1()).getWinStatus() == WINNER) ? "Congratulations! you Won!!!" :
                    ((HumanPlayer) board.getP1()).getWinStatus() == STALEMATE ? "Congratulations, you reached a Stalemate" :
                            "Sorry, you were beaten, better luck next time";
            JLabel gameover = new JLabel(gOver);
            gblGroupInner.addToList(new GBLLeaf(0, 2, 2, 1, new Insets(0, 30, 0, 0), gameover));

            JLabel pScore = new JLabel("Your score");
            gblGroupInner.addToList(new GBLLeaf(0, 3, 2, 1, new Insets(50, 50, 10, 0), pScore));

            // adds current player score to layout
            playerScore = new PlayerScore(board.getP1());
            name = new JLabel(playerScore.getName());
            time = new JLabel(getClockFormat(playerScore.getTime()));
            score = new JLabel(playerScore.getScore());
            gblGroupInner.addToList(new GBLLeaf(0, 4, name));
            gblGroupInner.addToList(new GBLLeaf(1, 4, time));
            gblGroupInner.addToList(new GBLLeaf(2, 4, score));
            scores.addScore(playerScore);
        }


        // adds top 5 scores, if 5 scores exist
        JLabel topScores = new JLabel("Top 5 Scores");
        gblGroupInner.addToList(new GBLLeaf(0,5,2,1, new Insets(50,70,0,10), topScores));
        int itCount = 0;
        if (scores.getScores().size() > 0)
        {
            int topScoresCount = Math.min(scores.getScores().size(), 5);
            for(Iterator<PlayerScore> scoreIt = scores.getScores().descendingIterator(); scoreIt.hasNext() && itCount < topScoresCount;)
            {
                PlayerScore nextScore = scoreIt.next();
                name = new JLabel(nextScore.getName());
                time = new JLabel(getClockFormat(nextScore.getTime()));
                score = new JLabel(nextScore.getScore());
                if (nextScore.equals(playerScore))
                {
                    Color topScoreCol = new Color(127, 67, 196);
                    name.setForeground(topScoreCol);
                    time.setForeground(topScoreCol);
                    score.setForeground(topScoreCol);
                }
                Insets topPadding = new Insets(10,10,0,10);
                if (itCount == topScoresCount-1) topPadding = new Insets(10,10,10,10);
                gblGroupInner.addToList(new GBLLeaf(0,6+itCount, 1, 1, topPadding, name));
                gblGroupInner.addToList(new GBLLeaf(1, 6+itCount, 1, 1, topPadding, time));
                gblGroupInner.addToList(new GBLLeaf(2, 6+itCount, 1, 1, topPadding, score));
                itCount += 1;
            }
        }

        JButton exit = new JButton("Close");
        exit.addActionListener(board.getUi().buttonH);
        gblGroupInner.addToList(new GBLLeaf(0, 7+itCount, 3, 1, exit));

        // sets background black and opaque, as using overlaylayout to show ontop of chess board
        setBackground(new Color((float) 0.0,(float) 0.0, (float) 0.0, (float) 0.6));
        scores.writeScores();
        gblGroupInner.addGBL(gbcInner, gblInner, inner);
        gblGroupPrimary.addGBL(gbcPrimary, gblPrimary, this);
    }

    private String getClockFormat(int milliseconds)
    {
        int totSeconds = milliseconds / 1000;
        int hours = totSeconds / 3600;
        int minutes = (totSeconds % 3600) / 60;
        int seconds = (totSeconds % 3600) % 60;
        return ((hours > 0)? hours + " hrs, ": "0 hrs, ") + ((minutes > 0)? minutes + " mins, ": "0 mins, ")
                + seconds + " secs.";
    }
}

