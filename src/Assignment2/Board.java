package Assignment2;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

import static Assignment2.Team.*;

/*
 * Board is the main controller (design Pattern) pertaining to the board array of tiles and instigating moves. Initially
 * the class was a facade controller. But it was later decided to implement moving actual pieces in the player class in
 * attempt to have role controller. However there is very high coupling given player classes talk to tiles through board.
 *
 * A players move is split into two parts:- considering a move, and making a move
 *
 * In the consideration phase, an input coordinate is given and all of the available moves for a piece related
 * to that coordinate is calculated and added to an array 'movingPiecesMoves' for the player to decide where to move
 * a piece.
 *
 * In the move phase, the move is made, the players turn is changed, and the pieces a new player can move is calculated
 * or, if the other player is AI, the ai's move making system is called.
 *
 * During both phases the player class is used to determine what actual moves can be made in accordence to chess's
 * rules.
 *
 * Upon initialization the players are created, ai or human, and pieces are initialized through the Player class.
 */
public class Board {
    Tile[][] board = new Tile[8][8]; // Tile array containing each individual tile
    private ChessUI ui; // reference to the ui, used for repainting mainly
    private Player p1; // player instance
    private Player p2; // player instance
    Player playersTurn; // a reference to the current players turn
    Player opponent; // a reference to the player not currently in turn
    private HashSet<Coord> movingPiecesMoves; // a set of coordinates a piece can move to
    private HashSet<Coord> availablePiecesToMove; // a set of coordinates to a piece the player can move
    private LinkedList<Board> previousBoardStates = new LinkedList<>(); // a set of board states of previous move instances
    private Tile movingTile; // the tile of which belonging to a piece in move
    private Piece movingPiece; // a piece that is currently being moved
    private boolean gameOver = false; // boolean indicating whether the game is finished
    private Player winner; // player instance for who has won if game is over
    boolean aiMakingMove; // boolean indicating ai player is moving (no repaints or other ui events to be done)
    final Object lock = new Object();

    /*
     * Default Board constructor, contains parameters required for instantiating player 1, and player 2
     *
     * @params
     *      ui, reference to the main ui instance mainly for repainting and access for other classes
     *      teamCol, the colour player 1 has chosen in text form
     *      op, the type of opponent the player has chosen to play against
     *      name, the players given name
     */
    Board(ChessUI ui, String teamCol, String op, String name)
    {
        movingPiecesMoves = new HashSet<>();
        // instantiates JPanel tiles
        TileHandler th = new TileHandler(this);
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                Tile tile = new Tile(new Coord(j, i));
                tile.addMouseListener(th);
                board[i][j] = tile;
            }
        }
        availablePiecesToMove = new HashSet<>();
        // oinstantiates players and sets playersTurn/opponent
        switch (teamCol)
        {
            case "white":
                p1 = new HumanPlayer(WHITE, P1, this);
                p2 = op.equals("human")? new HumanPlayer(BLACK, P2, this):
                        new AIPlayer(BLACK, P2, this);
                playersTurn = p1;
                opponent = p2;
                ((HumanPlayer) p1).startTime();
                break;
            case "black":
                p1 = new HumanPlayer(BLACK, P1, this);
                p2 = op.equals("human")? new HumanPlayer(WHITE, P2, this):
                        new AIPlayer(WHITE, P2, this);
                if (!(op.equals("human")))
                {
                    p2 = new AIPlayer(WHITE, P2, this);
                }
                playersTurn = p2;
                opponent = p1;
                break;
        }
        p1.setName(name);
        p2.setName("Player 2");
        this.ui = ui;
        // calls ai to make move if p2 = ai
        if (p2 instanceof AIPlayer && playersTurn == p2)
        {
            ((AIPlayer) p2).makeMove();
        }
        else availablePiecesToMove = playersTurn.getAvailableMovingPiecesCoords();
    }

    // Copy constructor, copies a board state used both in revert move and for ai to revert a move in minimax
    Board(Board board)
    {
        previousBoardStates = new LinkedList<>();
        for (int i = board.previousBoardStates.size()-1; i != -1; i--)
        {
            previousBoardStates.push(board.previousBoardStates.get(i));
        }
        gameOver = board.gameOver;
        winner = board.winner;
        TileHandler th = new TileHandler(this);
        this.board = new Tile[8][8];
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                Tile tile = new Tile(new Coord(j, i));
                tile.addMouseListener(th);
                this.board[i][j] = tile;
            }
        }
        availablePiecesToMove = new HashSet<>();
        p1 = new HumanPlayer(board.getP1(), this);
        p2 = (board.p2.isHuman())? new HumanPlayer(board.getP2(), this):
                new AIPlayer((AIPlayer) board.getP2(), this);
        if (board.getPlayersTurn().getPlayer() == P1)
        {
            playersTurn = p1;
            opponent = p2;
        }
        else
        {
            playersTurn = p2;
            opponent = p1;
        }
    }

    /*
     * considerMove's main purpose is to, given a position input from the player, to calculate moves that a piece
     * can make and set the tile to which they belongs colour indicating to the player they can move the piece there.
     *
     * To decide where a piece can move to, the Player class's consider move is called to calculate possible moves
     *
     * @param
     *      origin, the position a player has chosen related to a piece they can move
     */
    void considerMove(Coord origin)
    {
        playersTurn.setMoving(true);
        movingTile = getTile(origin);
        movingPiece = movingTile.getTilePiece();
        movingPiecesMoves = playersTurn.getPieceMoves(movingPiece);

        // sets tile colours
        if (!((aiMakingMove)))
        {
            movingPiecesMoves.add(origin);
            for (Coord move : movingPiecesMoves)
            {
                Tile potentialMove = getTile(move);
                if (potentialMove.isOccupied() && playersTurn.getTeam() != potentialMove.getTilePiece().teamPiece)
                    potentialMove.setColourHostile();
                else getTile(move.x, move.y).setColourMoving();
            }
        }

        if (!aiMakingMove) {
            ui.repaint();
        }
   }

   /*
    * makeMove's main purpose is to make the move. It determines if a player is castling, or if they are simply
    * replacing the piece in its original position, or making a move.
    *
    * A check is made towards the end to determine if the next player is out of moves and if gameover is reached
    */
    void makeMove (Coord destination)
    {

        // stops the clock for p1
        if (playersTurn == p1 && !aiMakingMove) ((HumanPlayer) p1).endTime();
        // if player replaces piece to where it came from, just replaces it
        if (destination.equals(movingTile.getTileCoord()) && !aiMakingMove && !playersTurn.isCastling())
        {
            getTile(destination).setPiece(movingPiece);
            clearColouredTiles();
            ui.repaint();
            movingPiecesMoves = new HashSet<>();
            playersTurn.setMoving(false);
            return;
        }
        // determines if a player is castling, to which the player is still taking a turn and is given the choice
        // as to where they want to place the king (almost broke the ai due to considerations, initially the board
        // automatically moved the king if they could castle)
        if (playersTurn.canCastle() && destination.equals(playersTurn.getCastleCastlingDestination()))
        {
            playersTurn.setCastling(true);
            movingPiecesMoves = new HashSet<>();
            movingPiecesMoves.add(playersTurn.getKing().getPosition());
            movingPiecesMoves.add(playersTurn.getKingCastleDestination());
            getTile(playersTurn.getKing().getPosition()).setColourMoving();
            getTile(playersTurn.getKingCastleDestination()).setColourMoving();
            playersTurn.movePiece(movingPiece, movingPiece.getPosition(), destination);
            movingPiece = playersTurn.getKing();
            movingTile = getTile(playersTurn.getKing().getPosition());
            return;
        }
        // places the king in new position, well, makes a new king due to bugs and the old king not accepting
        // new coordinates, or refusing them at a later move
        if (playersTurn.isCastling())
        {
            King newK = new King(destination, playersTurn.getTeam(), playersTurn.getColour());
            playersTurn.getPlayerPieces().remove(playersTurn.getKing());
            getTile(playersTurn.getKing().getPosition()).clearPiece();
            playersTurn.king = newK;
            playersTurn.getPlayerPieces().add(newK);
            movingPiece = newK;
            playersTurn.setCastling(false);
            playersTurn.setCanCastle(false);
        }

        // resets member variables and moving status
        playersTurn.setMoving(false);
        availablePiecesToMove = new HashSet<>();
        movingPiecesMoves = new HashSet<>();

        // adds copy board to previous moves for move reverting (mainly used for ai/debugging)
        previousBoardStates.push(new Board(this));

        // makes the move
        playersTurn.movePiece(movingPiece, movingPiece.getPosition(), destination);

        // Changes playersTurn to opponent, and resets moving status, clears tile colours
        Player temp = playersTurn;
        playersTurn = opponent;
        opponent = temp;
        if (playersTurn instanceof HumanPlayer) availablePiecesToMove.addAll(getPlayersTurn().getAvailableMovingPiecesCoords());

        if (playersTurn == p1 && !aiMakingMove) ((HumanPlayer) p1).startTime();

        // win check
        if (playersTurn.outOfMoves()) {
            if (playersTurn.king.isUnderThreat()) {
                gameOver = true;
                winner = opponent;
            } else
            {
                winner = null;
                gameOver = true;
            }
            if (winner != null)
            {
                if (winner == p1) ((HumanPlayer) p1).setWinStatus(WINNER);
                else ((HumanPlayer) p1).setWinStatus(null);
            }
            else ((HumanPlayer) p1).setWinStatus(STALEMATE);
            if (!aiMakingMove)
            {
                ui.initScores();
            }
        }

        // last minute addition, utilizes threads so that messages can be synchronized
        Thread thread = null;
        // calls ai player to make a move if their turn
        if (playersTurn instanceof AIPlayer && !aiMakingMove && !gameOver)
        {
            Runnable task = () -> {
                ((AIPlayer) playersTurn).makeMove();
            };
            thread = new Thread(task);
            thread.start();
        }

        // displays players turn message
        if (!gameOver && !aiMakingMove && !playersTurn.getKing().isUnderThreat())
        {
            ui.displayMessage(new JLabel(playersTurn.getTeam() + "'s turn"));
        }

        if (!aiMakingMove) {
            clearColouredTiles();
        }
    }

    public void takeMove(Board previous)
    {
//        System.out.println("hey");
//        System.out.println(previous.previousMoves.size());
        previousBoardStates = new LinkedList<>();
        for (int i = previous.previousBoardStates.size()-1; i != -1; i--)
        {
            previousBoardStates.push(previous.previousBoardStates.get(i));
        }
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                board[i][j].clearPiece();
            }
        }
        p1 = new HumanPlayer(previous.getP1(), this);
        p2 = new AIPlayer((AIPlayer) previous.getP2(), this);
        if (previous.playersTurn.getPlayer() == P1)
        {
            playersTurn = p1;
            opponent = p2;
        }
        else
        {
            playersTurn = p2;
            opponent = p1;
        }
        gameOver = previous.gameOver;
        winner = null;
        aiMakingMove = previous.aiMakingMove;
        ui.repaint();
    }

    /*
     * revertMove's purpose is to revert the board to a previous state utilizing previousBoardStates
     */
    public void revertMove()
    {
        Board previous = previousBoardStates.pop();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                board[i][j].clearPiece();
            }
        }
        p1 = new HumanPlayer(previous.getP1(), this);
        p2 = (previous.getP2().getPlayerType()== AI)? new AIPlayer((AIPlayer) previous.getP2(), this):
                new HumanPlayer(previous.getP2(), this);
        if (previous.playersTurn.getPlayer() == P1)
        {
            playersTurn = p1;
            opponent = p2;
        }
        else
        {
            playersTurn = p2;
            opponent = p1;
        }
        gameOver = previous.gameOver;
        winner = null;
        if (!!aiMakingMove)
        {
            clearColouredTiles();
            ui.repaint();
        }
        availablePiecesToMove = playersTurn.getAvailableMovingPiecesCoords();
    }

    JLabel genMessage(String message)
    {
        return new JLabel(message);
    }
    /*
     * clearColouredTiles purpose is to loop over all tiles and set their colour to default
     */
    void clearColouredTiles()
    {
        for (int i = 0; i < board.length; i++)
        {
            for (Tile tile: board[i])
            {
                tile.setColourDefault();
            }
        }
        ui.revalidate();
        ui.repaint();
    }

    // the remaining functions are simple get/set methods
    boolean isOccupied(int x, int y)
    {
        return (board[y][x].isOccupied());
    }

    Tile getTile(Coord coord)
    {
        return board[coord.y][coord.x];
    }

    Tile getTile(int x, int y)
    {
        return board[y][x];
    }

    void setAIMakingMove(Boolean moving)
    {
        aiMakingMove = moving;
    }

    ChessUI getUi() {
        return ui;
    }

    Tile getMovingTile() {
        return movingTile;
    }

    Player getPlayersTurn() {
        return playersTurn;
    }

    LinkedList<Board> getPreviousBoardStates() {
        return previousBoardStates;
    }

    Player getOpponent()
    {
        return opponent;
    }

    Player getP1() {
        return p1;
    }

    Player getP2() {
        return p2;
    }

    Set<Coord> getMovingPiecesMoves()
    {
        return movingPiecesMoves;
    }

    Piece getMovingPiece()
    {
        return movingPiece;
    }

    boolean isGameOver()
    {
        return gameOver;
    }

    Player getWinner()
    {
        return winner;
    }

    public HashSet<Coord> getAvailablePiecesToMove() {
        return availablePiecesToMove;
    }

    // overrides the toString method, mainly used in debugging
    @Override
    public String toString()
    {
        String out = "\n";
        for (int i = 0; i < 8; i++)
        {
            out += "[";
            for (int j = 0; j < 8; j++)
            {
                out += ((board[i][j].isOccupied()? board[i][j].getTilePiece() + (j!=7? "|": ""):  "      " + (j!=7? "|":"")));
            }
            out += "]\n---------------------------------------------------------\n";
        }
        return out;
    }
}
