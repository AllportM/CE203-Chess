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
    private Set<Coord> potentialMoves;
    private LinkedList<Board> previousMoves = new LinkedList<>();
    private Tile movingTile;
    private Piece movingPiece;
    private boolean gameOver = false;
    private Player winner;
    private boolean AIEval;
    private boolean AIMakingMove;

    Board(ChessUI ui, String teamCol, String op, String name)
    {
        potentialMoves = new HashSet<>();
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
        switch (teamCol)
        {
            case "white":
                p1 = new HumanPlayer(WHITE, P1, this);
                p2 = op.equals("human")? new HumanPlayer(BLACK, P2, this):
                        new AIPlayer(BLACK, P2, this);
                playersTurn = p1;
                opponent = p2;
                break;
            case "black":
                p1 = new HumanPlayer(BLACK, P1, this);
                p2 = op.equals("human")? new HumanPlayer(WHITE, P2, this):
                        new AIPlayer(WHITE, P2, this);
                playersTurn = p2;
                opponent = p1;
                break;
        }
        p1.setName(name);
        p2.setName("Player 2");
        this.ui = ui;

//        Queen test1 = new Queen(new Coord(3,3), P2, p2.colour);
//        p2.addPiece(test1);
//        board[3][3].setPiece(test1);
//        Board testboard = new Board(this);
//        System.out.println(testboard.getPlayersTurn());
        // test stalemate move white move first
//        for (Iterator<Piece> it = p1.getPlayerPieces().iterator(); it.hasNext();)
//        {
//            Piece piece = it.next();
//            it.remove();
//        }
//        for (Iterator<Piece> it = p2.getPlayerPieces().iterator(); it.hasNext();)
//        {
//            Piece piece = it.next();
//            it.remove();
//        }
//        for (int i = 0; i < board.length; i++)
//        {
//            for (Tile tile: board[i])
//            {
//                tile.clearPiece();
//            }
//        }
//        King king = new King(new Coord(1, 2), p1.getPlayer(), WHITE);
//        board[2][1].setPiece(king);
//        p1.getPlayerPieces().add(king);
//        p1.setKing(king);
//        Castle castle = new Castle(new Coord(3, 2), p1.getPlayer(), WHITE);
//        board[2][3].setPiece(castle);
//        p1.getPlayerPieces().add(castle);
//        Queen queen = new Queen(new Coord(5, 4), p1.getPlayer(), WHITE);
//        p1.getPlayerPieces().add(queen);
//        board[4][5].setPiece(queen);
//        King kingB = new King(new Coord(0,0), p2.getPlayer(), BLACK);
//        p2.getPlayerPieces().add(kingB);
//        p2.setKing(kingB);
//        board[0][0].setPiece(kingB);

                // test stalemate black move
//        King king = new King(new Coord(1, 2), p2.getPlayer(), BLACK);
//        board[2][1].setPiece(king);
//        p2.getPlayerPieces().add(king);
//        p2.setKing(king);
//        Castle castle = new Castle(new Coord(3, 1), p2.getPlayer(), BLACK);
//        board[1][2].setPiece(castle);
//        p2.getPlayerPieces().add(castle);
//        Queen queen = new Queen(new Coord(5, 4), p2.getPlayer(), BLACK);
//        p2.getPlayerPieces().add(queen);
//        board[4][5].setPiece(queen);
//        King kingB = new King(new Coord(0,0), p1.getPlayer(), WHITE);
//        p1.getPlayerPieces().add(kingB);
//        p1.getPlayerPieces().add(kingB);
//        board[0][1].setPiece(kingB);
    }

    // Copy constructor
    Board(Board board)
    {
        previousMoves = new LinkedList<>();
        for (int i = board.previousMoves.size()-1; i != -1; i--)
        {
            previousMoves.push(board.previousMoves.get(i));
        }
        ui = board.ui;
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
//        System.out.println("Suspicous tile considering " + getTile(4, 7) + "\n");
        //saves move for reverting
        playersTurn.setMoving(true);
        movingTile = getTile(origin);
        movingPiece = movingTile.getTilePiece();
//        System.out.println("player clicked is:- \n" + playersTurn);
//        System.out.println(getTile(origin).getTilePiece().isProtector());
//        System.out.println(movingTile);
        potentialMoves = playersTurn.getPieceMoves(movingPiece);
        if (!AIEval && !AIMakingMove && !playersTurn.getKing().isUnderThreat()) potentialMoves.add(origin);

        // sets tile colours
        if (!((AIEval || AIMakingMove)))
        {
            for (Coord move : potentialMoves)
            {
                Tile potentialMove = getTile(move);
                if (potentialMove.isOccupied() && playersTurn.getTeam() != potentialMove.getTilePiece().teamPiece
                        && (!AIEval && !AIMakingMove))
                    potentialMove.setColourHostile();
                else if (!AIEval && !AIMakingMove) getTile(move.x, move.y).setColourMoving();
            }
        }

        if (!AIEval && !AIMakingMove) {
            movingTile.clearPiece();
            ui.repaint();
        }
        if (AIMakingMove) {
            movingTile.clearPiece();
        }
       }

    void makeMove (Coord destination)
    {
        playersTurn.setMoving(false);
        // if player replaces piece to where it came from, just replaces it
        if (destination.equals(movingTile.getTileCoord()))
        {
            getTile(destination).setPiece(movingPiece);
            clearColouredTiles();
            ui.repaint();
            return;
        }

        // adds copy board to previous moves for move reverting (mainly used for ai/debugging)
        previousMoves.push(new Board(this));

        playersTurn.movePiece(movingPiece, movingPiece.getPosition(), destination);
//        System.out.println("MOVED " + getTile(4, 7));

        if (!AIMakingMove) clearColouredTiles();

        movingPiece = null;
        movingTile = null;
        potentialMoves = new HashSet<>();

//        System.out.println("MOVED " + getTile(4, 7));

        // Changes playersTurn to opponent, and resets moving status, clears tile colours
        if (playersTurn.getPlayer() == P1) {
            playersTurn = p2;
            opponent = p1;
        } else {
            playersTurn = p1;
            opponent = p2;
        }

//        System.out.println("MOVED " + getTile(4, 7));

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
        }

//        System.out.println("MOVED " + getTile(4, 7));

        if (!AIEval && !AIMakingMove) {
            ui.repaint();
        }


//        System.out.println("MOVED " + getTile(4, 7));
//        System.out.println(playersTurn);
//        System.out.println("player clicked is:- \n" + playersTurn);
    }

    public void takeMove(Board previous)
    {
//        System.out.println("hey");
//        System.out.println(previous.previousMoves.size());
        previousMoves = new LinkedList<>();
        for (int i = previous.getPreviousMoves().size()-1; i != -1; i--)
        {
            previousMoves.push(previous.getPreviousMoves().get(i));
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
        AIEval = previous.AIEval;
        ui.repaint();
    }

    public void revertMove()
    {
//        System.out.println(previousMoves.size());
        Board previous = previousMoves.pop();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                board[i][j].clearPiece();
            }
        }
        p1 = new HumanPlayer(previous.getP1(), this);
        p2 = (previous.getP2().getPlayerType()== AI)? new AIPlayer((AIPlayer) previous.getP2(), this):
                new HumanPlayer((HumanPlayer) previous.getP2(), this);
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
        AIEval = previous.AIEval;
        ui.repaint();
    }


    private void clearColouredTiles()
    {
        for (Coord reset: potentialMoves)
        {
            getTile(reset.x, reset.y).setColourDefault();
        }
        movingTile.setColourDefault();
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

    void setAIEval(Boolean evaluating)
    {
        AIEval = evaluating;
    }

    void setAIMakingMove(Boolean moving)
    {
        AIMakingMove = moving;
    }

    public ChessUI getUi() {
        return ui;
    }

    public Tile getMovingTile() {
        return movingTile;
    }

    Player getPlayersTurn() {
        return playersTurn;
    }

    public LinkedList<Board> getPreviousMoves() {
        return previousMoves;
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

    Set<Coord> getPotentialMoves()
    {
        return potentialMoves;
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
