package Assignment2;

import java.util.HashSet;
import java.util.Set;

import static Assignment2.Team.*;

public class Board {
    Tile[][] board = new Tile[8][8];
    private ChessUI ui;
    private Player p1;
    private Player p2;
    private Player playersTurn;
    private Player opponent;
    private Team moveStatus;
    private Tile movingTile;
    private Piece movingPiece;
    private Set<Coord> potentialMoves;
    private boolean p1Castling = false;
    private boolean p2Castling = false;
    private boolean p1CantCastle = false;
    private boolean p2CantCastle = false;
    private boolean gameOver = false;
    private Player winner;

    Board(ChessUI ui, String teamCol, String op, String name)
    {
        switch (teamCol)
        {
            case "white":
                p1 = new Player(WHITE, P1);
                p2 = new Player(BLACK, P2);
                playersTurn = p1;
                opponent = p2;
                break;
            case "black":
                p1 = new Player(BLACK, P1);
                p2 = new Player(WHITE, P2);
                playersTurn = p2;
                opponent = p1;
                break;
        }
        if (op.equals("human")) p2.setType(HUMAN);
        else p2.setType(AI);
        p1.setType(HUMAN);
        p1.setName(name);
        p2.setName("Player 2");
        this.ui = ui;
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

        initPlayerPieces();
        Queen test1 = new Queen(new Coord(3,3), P2, p2.colour);
        p2.addPiece(test1);
        board[3][3].setPiece(test1);
    }

    protected boolean isOccupied(int x, int y)
    {
        return (board[y][x].isOccupied());
    }

    protected Tile getTile(int x, int y)
    {
        return board[y][x];
    }

    private void initPlayerPieces()
    {
        for (int y = 0; y < board.length; y++)
        {
            for (Tile t: board[y])
            {
                if (y == 1)
                {
                    Pawn newP = new Pawn(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newP);
                    p2.addPiece(newP);
                }
                if (y == 6)
                {
                    Pawn newP = new Pawn(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newP);
                    p1.addPiece(newP);
                }
                if (y == 0 && (t.getTileCoord().x == 0 || t.getTileCoord().x == 7))
                {
                    Castle newC = new Castle(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newC);
                    p2.addPiece(newC);
                }
                if (y==7 && (t.getTileCoord().x ==0 || t.getTileCoord().x == 7))
                {
                    Castle newC = new Castle(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newC);
                    p1.addPiece(newC);
                }
                if (y==0 && (t.getTileCoord().x == 1 || t.getTileCoord().x == 6))
                {
                    Horse newH = new Horse(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newH);
                    p2.addPiece(newH);
                }
                if (y==7 && (t.getTileCoord().x == 1 || t.getTileCoord().x == 6))
                {
                    Horse newH = new Horse(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newH);
                    p1.addPiece(newH);
                }
                if (y==0 && (t.getTileCoord().x == 2 || t.getTileCoord().x == 5))
                {
                    Bishop newB = new Bishop(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newB);
                    p2.addPiece(newB);
                }
                if (y==7 && (t.getTileCoord().x == 2 || t.getTileCoord().x == 5))
                {
                    Bishop newB = new Bishop(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newB);
                    p1.addPiece(newB);
                }
                if (y==0 && t.getTileCoord().x == 3)
                {
                    Queen newQ = new Queen(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newQ);
                    p2.addPiece(newQ);
                }
                if (y==7 && t.getTileCoord().x == 3)
                {
                    Queen newQ = new Queen(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newQ);
                    p1.addPiece(newQ);
                }
                if (y==0 && t.getTileCoord().x == 4)
                {
                    King newK = new King(t.getTileCoord(), P2, p2.colour);
                    t.setPiece(newK);
                    p2.addPiece(newK);
                    p2.setKing(newK);
                }
                if (y==7 && t.getTileCoord().x == 4)
                {
                    King newK = new King(t.getTileCoord(), P1, p1.colour);
                    t.setPiece(newK);
                    p1.addPiece(newK);
                    p1.setKing(newK);
                }
            }
        }
    }

    void considerMove(Tile tile) {
        potentialMoves = new HashSet<>();
        playersTurn.setMoving(true);
        movingTile = tile;
        movingPiece = tile.getTilePiece();
        int moveX = tile.getTileCoord().x;
        int moveY = tile.getTileCoord().y;
        System.out.println(movingPiece + "\n + " + movingPiece.isProtector() + "\nKings threat is "
                + ((movingPiece.teamPiece == P1)? p1.getKing().isUnderThreat(): p2.getKing().isUnderThreat()) + "\n");
        if (movingPiece instanceof King) {
            potentialMoves = getKingsMoves(playersTurn);
        } else if (movingPiece.isProtector()) {
            potentialMoves = getProtectorsMoves(movingPiece);
        } else if (placesInCheck(movingPiece, ((movingPiece.teamPiece == P1)? getP1(): getP2()),
                ((movingPiece.teamPiece == P1)? getP2(): getP1()))){/*do nothing*/}
        else
        {
            // removes invalid king moves due to conflicts
            switch (movingPiece.teamPiece) {
                case P1:
                    if (!getP1().getKing().isUnderThreat()) potentialMoves = movingPiece.getValidMoves(this);
                    // sets p1 castling status allowing makeMove to make castling move
                    if (!(p1CantCastle)) {
                        if (movingPiece instanceof Castle && moveX == 0 && moveY == 7
                                && ((Castle) movingPiece).isFirstMove() && !getTile(1, 7).isOccupied()
                                && !getTile(2, 7).isOccupied() && !getTile(3, 7).isOccupied()
                                && getTile(4, 7).isOccupied() && getTile(4, 7).getTilePiece() instanceof King
                                && ((King) getTile(4, 7).getTilePiece()).isFirstMove())
                            p1Castling = true;
                        else if (movingPiece instanceof Castle && moveX == 7 && moveY == 7
                                && ((Castle) movingPiece).isFirstMove() && !getTile(6, 7).isOccupied()
                                && !getTile(5, 7).isOccupied() && getTile(4, 7).isOccupied()
                                && getTile(4, 7).getTilePiece() instanceof King
                                && ((King) getTile(4, 7).getTilePiece()).isFirstMove())
                            p1Castling = true;
                    }
                case P2:
                    if (!getP2().getKing().isUnderThreat()) potentialMoves = movingPiece.getValidMoves(this);
                    // sets p1 castling status allowing makeMove to make castling move
                    if (!(p2CantCastle)) {
                        if (movingPiece instanceof Castle && moveX == 0 && moveY == 0
                                && ((Castle) movingPiece).isFirstMove() && !getTile(1, 0).isOccupied()
                                && !getTile(2, 0).isOccupied() && !getTile(3, 0).isOccupied()
                                && getTile(4, 0).isOccupied() && getTile(4, 0).getTilePiece() instanceof King
                                && (((King) getTile(4, 0).getTilePiece()).isFirstMove()))
                            p2Castling = true;
                        else if (movingPiece instanceof Castle && moveX == 7 && moveY == 0
                                && ((Castle) movingPiece).isFirstMove() && !getTile(6, 0).isOccupied()
                                && !getTile(5, 0).isOccupied() && getTile(4, 0).isOccupied()
                                && getTile(4, 0).getTilePiece() instanceof King
                                && (((King) getTile(4, 0).getTilePiece()).isFirstMove()))
                            p2Castling = true;
                    }
                    break;
            }
        }
        potentialMoves.add(movingPiece.getPosition());
        for (Coord move : potentialMoves) {
            Tile potentialMove = getTile(move.x, move.y);
            if (potentialMove.isOccupied() && potentialMove.getTilePiece().teamPiece != movingPiece.teamPiece)
                potentialMove.setColourHostile();
            else getTile(move.x, move.y).setColourMoving();
        }
        tile.clearPiece();
        ui.repaint();
    }

    void makeMove(Tile tile)
    {
        // if player replaces piece to where it came from, just replaces it
        if (tile.getTileCoord() == movingPiece.getPosition())
        {
            tile.setPiece(movingPiece);
            playersTurn.setMoving(false);
            clearColouredTiles();
            return;
        }

        // sets first move to false on pieces requiring first move tracking
        if (movingPiece instanceof Pawn && ((Pawn) movingPiece).isFirstMove())
            ((Pawn) movingPiece).setFirstMove();
        if (movingPiece instanceof Castle && ((Castle) movingPiece).isFirstMove())
        {
            ((Castle) movingPiece).setFirstMove();
            if (movingPiece.teamPiece == P1) p1CantCastle = true;
            if (movingPiece.teamPiece == P2) p2CantCastle = true;
        }
        if (movingPiece instanceof King && ((King) movingPiece).isFirstMove())
        {
            if (movingPiece.teamPiece == P1) p1CantCastle = true;
            if (movingPiece.teamPiece == P2) p2CantCastle = true;
            ((King) movingPiece).setFirstMove();
        }

        // destroys opponents piece if taken
        if (tile.isOccupied())
        {
            Piece destroyed = tile.getTilePiece();
            if (destroyed.teamPiece == P1) p1.removePiece(destroyed);
            else p2.removePiece(destroyed);
            tile.setPiece(movingPiece);
        }

        // Adds new queen if moving piece is a pawn and reached end of board, or adds piece to move tile
        if (movingPiece.teamPiece == P1 && movingPiece instanceof Pawn && tile.getTileCoord().y == 0)
        {
            p1.removePiece(movingPiece);
            Queen newQ = new Queen(new Coord(tile.getTileCoord().x, tile.getTileCoord().y), P1, p1.colour);
            p1.addPiece(newQ);
            tile.setPiece(newQ);
        }
        else if (movingPiece.teamPiece == P2 && movingPiece instanceof Pawn && tile.getTileCoord().y == 7)
        {
            p2.removePiece(movingPiece);
            Queen newQ = new Queen(new Coord(tile.getTileCoord().x, tile.getTileCoord().y), P2, p2.colour);
            p2.addPiece(newQ);
            tile.setPiece(newQ);
        }
        // or sets tile to moving piece
        else tile.setPiece(movingPiece);

        // actions castling moves by moving king pieces
        if (p1Castling)
        {
            if (tile.getTileCoord().x == 3  &&  tile.getTileCoord().y == 7)
            {
                King p1King = ((King) getTile(4, 7).getTilePiece());
                getTile(4, 7).clearPiece();
                getTile(2, 7).setPiece(p1King);
                p1Castling = false;
            }
            else if (tile.getTileCoord().x == 5 && tile.getTileCoord().y == 7)
            {
                King p1King = ((King) getTile(4, 7).getTilePiece());
                getTile(4, 7).clearPiece();
                getTile(6, 7).setPiece(p1King);
                p1Castling = false;
            }
        }
        if (p2Castling)
        {
            if (tile.getTileCoord().x == 3 && tile.getTileCoord().y == 0)
            {
                King p1King = ((King) getTile(4, 0).getTilePiece());
                getTile(4, 0).clearPiece();
                getTile(2, 0).setPiece(p1King);
                p2Castling = false;
            }
            else if (tile.getTileCoord().x == 5 && tile.getTileCoord().y == 0)
            {
                King p1King = ((King) getTile(4, 0).getTilePiece());
                getTile(4, 0).clearPiece();
                getTile(6, 0).setPiece(p1King);
                p2Castling = false;
            }
        }

        // checks for placing players into check
        Piece pieceMoved = tile.getTilePiece();
        Set<Coord> checkMoves = pieceMoved.getValidMoves(this);
        for (Coord move: checkMoves)
        {
            if (getTile(move.x, move.y).isOccupied() && getTile(move.x, move.y).getTilePiece() instanceof King)
            {
                if (movingPiece.teamPiece == P1)
                {
                    p2.getKing().setUnderThreat(movingPiece);
                    p2.getKing().setAttacker(movingPiece);
                }
                if (movingPiece.teamPiece == P2)
                {
                    p1.getKing().setUnderThreat(movingPiece);
                    p1.getKing().setAttacker(movingPiece);
                }
            }
        }

        // clears check status and protector status
        if ((movingPiece instanceof  King || movingPiece.isProtector()) && playersTurn.getKing().isUnderThreat())
        {
            playersTurn.getKing().clearUnderThreat();
            for (Piece piece: playersTurn.getPlayerPieces())
            {
                piece.setProtector(false);
            }
        }

        setWinDraw();
        // Changes playersTurn to opponent, and resets moving status, clears tile colours
        changePlayers();
        clearColouredTiles();
        ui.repaint();
    }

    private void changePlayers()
    {
        if (playersTurn.getPlayer() == P1)
        {
            playersTurn = p2;
            opponent = p1;
            p1.setMoving(false);
        }
        else
        {
            playersTurn = p1;
            opponent = p2;
            p2.setMoving(false);
        }
    }

    private boolean setWinDraw() {
        if (opponent.getKing().isUnderThreat())
        {
            boolean checkWin = isCheckMate();
            if (checkWin)
            {
                gameOver = true;
                winner = playersTurn;
                return true;
            }
        }
        else if (getKingsMoves(opponent).size() == 0)
        {
            boolean noMoves = true;
            for (Piece piece: playersTurn.getPlayerPieces())
            {
                if (!placesInCheck(piece, playersTurn, opponent) && piece.getValidMoves(this).size() > 0)
                {
                    noMoves = false;
                }
            }
            if (noMoves)
            {
                gameOver = true;
                winner = null;
                return true;
            }
        }
        return false;
    }

    private void clearColouredTiles()
    {
        for (Coord reset: potentialMoves)
        {
            getTile(reset.x, reset.y).setColourDefault();
        }
        movingTile.setColourDefault();
        ui.repaint();
    }

    private boolean isCheckMate()
    {
        boolean gotProtector = false;
        for (Piece piece: opponent.getPlayerPieces())
        {
            if (!(piece instanceof King))
            {
                Set<Coord> checkMoves = new HashSet<>();
                Coord enemiesPosition = opponent.getKing().getAttacker().getPosition();
                if (opponent.getKing().getValidMoves(this).contains(enemiesPosition)
                        && piece.getValidMoves(this).contains(enemiesPosition))
                    checkMoves.add(enemiesPosition);
                else if (!opponent.getKing().getValidMoves(this).contains(enemiesPosition)) {
                    checkMoves = piece.getValidMoves(this);
                    checkMoves.retainAll(opponent.getKing().getValidMoves(this));
                    checkMoves.retainAll(opponent.getKing().getAttacker().getValidMoves(this));
                }
                if (checkMoves.size() > 0) {
                    piece.setProtector(true);
                    gotProtector = true;
                } else piece.setProtector(false);
            }
        }
        if (getKingsMoves(opponent).size() == 0 && !gotProtector) return true;
        return false;
    }

    private Set<Coord> getProtectorsMoves(Piece piece)
    {
        Set<Coord> moves = piece.getValidMoves(this);
        if (piece.getValidMoves(this).contains(playersTurn.getKing().getAttacker().getPosition()))
        {
            moves.add(playersTurn.getKing().getAttacker().getPosition());
        }else {
            moves.retainAll(playersTurn.getKing().getValidMoves(this));
            moves.retainAll(playersTurn.getKing().getAttacker().getValidMoves(this));
        }
        return moves;
    }

    private Set<Coord> getKingsMoves(Player player)
    {
        Set<Coord> moves = new HashSet<>();
        switch (player.getPlayer())
        {
            case P1:
                moves = getP1().getKing().getValidMoves(this);
                for (Piece piece: opponent.getPlayerPieces())
                {
                    moves.removeAll(piece.getValidMoves(this));
                }
                break;
            case P2:
                moves = getP2().getKing().getValidMoves(this);
                for (Piece piece: getP1().getPlayerPieces())
                {
                    moves.removeAll(piece.getValidMoves(this));
                }
                break;
        }
        return moves;
    }

    private boolean placesInCheck(Piece piece, Player friendly, Player opponent)
    {
        if (!friendly.getKing().isUnderThreat())
        {
            Tile thisOnesTile = getTile(piece.getPosition().x, piece.getPosition().y);
            thisOnesTile.clearPiece();
            boolean kingInCheck = false;
            for (Piece pieceChecking: opponent.getPlayerPieces())
            {
                if(pieceChecking.getValidMoves(this).contains(friendly.getKing().getPosition()))
                {
                    kingInCheck = true;
                }
            }
            thisOnesTile.setPiece(piece);
            if (kingInCheck) return true;
        }
        return false;
    }

    Player getPlayersTurn() {
        return playersTurn;
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

    boolean isGameOver()
    {
        return gameOver;
    }

    Player getWinner()
    {
        return winner;
    }
}
