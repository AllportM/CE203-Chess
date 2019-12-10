package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static Assignment2.Team.P1;

public class ChessUI extends JFrame {
    Board chessBoard;
    JButton startGame;
    ButtonHandler buttonH;
    private MenuBar menu;

    public ChessUI()
    {
        Set<Coord> test = new HashSet<>();
        test.add(new Coord(1, 0));
        test.add(new Coord(2, 0));
        test.add(new Coord(3, 0));
        System.out.println(test.contains(new Coord(6, 1)));
        buttonH = new ButtonHandler(this);
        startGame = new JButton("I want to play a game...");
        menu = new MenuBar(this);
        setPreferredSize(new Dimension(800, 800));
        initPlayerWelcome();
        setJMenuBar(menu);
    }


    void initChess(String teamCol, String op, String name)
    {
        getContentPane().removeAll();
        setJMenuBar(menu);
        chessBoard = new Board(this, teamCol, op, name);
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < chessBoard.board.length; i++)
        {
            for (Tile tile: chessBoard.board[i])
            {
                board.add(tile);
            }
        }
        add(board);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
        repaint();
    }

    void initPlayerWelcome()
    {
        if (getContentPane() != null) getContentPane().removeAll();
        setJMenuBar(menu);
        JPanel centre = new PlayerWelcome(this);
        add(centre, BorderLayout.CENTER);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
    }

    public static void main(String[] args) {
        ChessUI current = new ChessUI();
        current.repaint();
    }

    public void setChessBoard(Board b)
    {
        chessBoard = b;
    }

    public MenuBar getMenu() {
        return menu;
    }
}

class Tile extends JPanel {
    private Piece tilePiece;
    private Rectangle background;
    private Color toPaint;
    private final Color hostileCol = new Color(201, 36, 36);
    private final Color movingColour = new Color(50, 166, 108);
    private final Color movePieceColour = new Color(127, 67, 196);
    private final Color defaultCol;
    private final Coord tileCoord;
    private final int width = 100;
    private final int height = 100;

    Tile(Tile tile)
    {
        tilePiece = tile.tilePiece;
        defaultCol = tile.defaultCol;
        tileCoord = tile.tileCoord;
        toPaint = tile.toPaint;
        background = tile.background;
    }

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
        if (this.isOccupied()) clearPiece();
        tilePiece = piece;
    }

    Color getToPaint()
    {
        return toPaint;
    }

    Color getDefaultCol()
    {
        return defaultCol;
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

    @Override
    public void paintComponent(Graphics g)
    {
        background.renderShape(g);
        if (tilePiece != null) tilePiece.drawing.renderShape(g);
    }

    public String toString()
    {
        return "I am a Tile, my piece is:- \n" + getTilePiece() +"\nMy posittion is:- \n" + getTileCoord();
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
        int piece1 = (int) (Math.random() * 6);
        Tile whiteSample = new Tile(new Coord(0, 0));
        whiteSample.setPiece(getPiece(piece1, 1));
        int piece2 = (int) (Math.random() * 6);
        Tile blackSample = new Tile(new Coord(2, 0));
        blackSample.setPiece(getPiece(piece2, 2));
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

    private Piece getPiece(int value, int team)
    {
        Team col = (team == 1)? Team.WHITE: Team.BLACK;
        switch(value)
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
