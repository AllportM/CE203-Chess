package Assignment2;

import java.util.*;

import static Assignment2.Team.*;

public class Board {
    Tile[][] board = new Tile[8][8];
    public final static int DIFFICULTY = 2;
    private ChessUI ui;
    private Player p1;
    private Player p2;
    Player playersTurn;
    Player opponent;
    private HashSet<Coord> movingPiecesMoves;
    private HashSet<Coord> availablePiecesToMove;
    private LinkedList<Board> previousBoardStates = new LinkedList<>();
    private Tile movingTile;
    private Piece movingPiece;
    private boolean gameOver = false;
    private Player winner;
    private boolean aiMakingMove;

    Board(ChessUI ui, String teamCol, String op, String name)
    {
        movingPiecesMoves = new HashSet<>();
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
        if (p2 instanceof AIPlayer && playersTurn == p2)
        {
            ((AIPlayer) p2).makeMove();
        }
        else availablePiecesToMove = playersTurn.getAvailableMovingPiecesCoords();
    }

    // Copy constructor
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

    void makeMove (Coord destination)
    {

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
        playersTurn.setMoving(false);
        availablePiecesToMove = new HashSet<>();

        // adds copy board to previous moves for move reverting (mainly used for ai/debugging)
        previousBoardStates.push(new Board(this));

        playersTurn.movePiece(movingPiece, movingPiece.getPosition(), destination);
        movingPiecesMoves = new HashSet<>();


        // Changes playersTurn to opponent, and resets moving status, clears tile colours
        Player temp = playersTurn;
        playersTurn = opponent;
        opponent = temp;
        if (playersTurn instanceof HumanPlayer) availablePiecesToMove.addAll(getPlayersTurn().getAvailableMovingPiecesCoords());

        if (playersTurn == p1 && !aiMakingMove) ((HumanPlayer) p1).startTime();

        movingPiecesMoves = new HashSet<>();


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
                System.out.println(this);
                System.out.println("P1 king pos " + getP1().getKing().getPosition());
                System.out.println("P2 king pos " + getP2().getKing().getPosition());
                ui.initWinner();
            }
        }
        else if (playersTurn instanceof AIPlayer && !aiMakingMove)
        {
            ((AIPlayer) playersTurn).makeMove();
        }

        if (!aiMakingMove) {
            clearColouredTiles();
        }

//        System.out.println("MOVED " + getTile(4, 7));




//        System.out.println("MOVED " + getTile(4, 7));
//        System.out.println(playersTurn);
//        System.out.println("player clicked is:- \n" + playersTurn);
    }

    public void takeMove(Board previous)
    {
//        System.out.println("hey");
//        System.out.println(previous.previousMoves.size());
        previousBoardStates = new LinkedList<>();
        for (int i = previous.getPreviousBoardStates().size()-1; i != -1; i--)
        {
            previousBoardStates.push(previous.getPreviousBoardStates().get(i));
        }
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                board[i][j].clearPiece();
            }
        }
        p1 = new HumanPlayer(previous.getP1(), this);
        if (p2.isHuman()) p2 = new HumanPlayer(previous.getP2(), this);
        else p2 = new AIPlayer((AIPlayer) previous.getP2(), this);
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
        p1.setMoving(false);
        p2.setMoving(false);
        availablePiecesToMove = playersTurn.getAvailableMovingPiecesCoords();
        gameOver = previous.gameOver;
        winner = null;
        clearColouredTiles();
        ui.repaint();
    }

    public void revertMove()
    {
//        System.out.println(previousMoves.size());
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

    boolean isOccupied(int x, int y)
    {
        return (board[y][x].isOccupied());
    }

    Tile getTile(Coord coord)
    {
        return board[coord.y][coord.x];
    }

    // Overloaded version for when specific coordinate information needed (castling for instance in Player)
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
