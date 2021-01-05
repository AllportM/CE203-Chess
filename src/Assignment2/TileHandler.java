package Assignment2;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * TileHandler's purpose is to implemet mouseListeners, highlighting tiles in which contain moves that
 * a player can make, and sending the coordinates of the tile's locations to the boards makeMove and considerMove
 * methods
 */
public class TileHandler implements MouseListener {
    private Board board; // reference to the board
    private boolean shortClick = false;

    // default constructor
    TileHandler(Board board)
    {
        this.board = board;
    }

    // overrides the method for when a mouse enters a Tile/Jpanel
    @Override
    public void mouseEntered(MouseEvent e)
    {
        board.getUi().keyListener.clearIndex();
        Player player = board.getPlayersTurn();
        Tile tileEntered = ((Tile)e.getComponent());
        // ensures the tile entered is a valid human move, and repaints accoringly
        if (!board.isGameOver() && player.isHuman())
        {
            if (tileEntered.isOccupied() && board.getAvailablePiecesToMove().contains(tileEntered.getTileCoord())
                    && !player.isMoving())
            {
                tileEntered.setColourMovePiece();
                board.getUi().repaint();
            }
            else if (player.isMoving() && board.getMovingPiecesMoves().contains(tileEntered.getTileCoord()))
            {
                tileEntered.setColourMovePiece();
                board.getUi().repaint();
            }
        }
    }

    // overrides the method for when a mouse leaves a Tile/Jpanel, resetting its colour
    @Override
    public void mouseExited(MouseEvent e) {
        Tile tileExited = ((Tile)e.getComponent());
        if (!board.isGameOver() && board.getPlayersTurn().isHuman())
        {
            if (!board.getPlayersTurn().isMoving())
            {
                tileExited.setColourDefault();
                board.getUi().repaint();
            }
            else if (board.getMovingPiecesMoves().contains(tileExited.getTileCoord()))
            {
                tileExited.setColourMoving();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // using mouse pressed and released given mouseclicked can be unpredictable
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!board.isGameOver() && !shortClick)
        {
            shortClick = true;
        }
    }

    // calls the considerMove/makeMove if valid
    @Override
    public void mouseReleased(MouseEvent e) {
        Player pTurn = board.getPlayersTurn();
        Tile tileClicked = ((Tile)e.getComponent());
        if (shortClick && pTurn.isHuman() && !board.isGameOver())
        {
            // makes move if human
            if (pTurn.isMoving() && ((board.getMovingPiecesMoves().contains(tileClicked.getTileCoord()))
                || tileClicked.getTileCoord().equals(board.getMovingTile().getTileCoord())))
            {
                board.clearColouredTiles();
                pTurn.board.makeMove(tileClicked.getTileCoord());
                board.getUi().repaint();
            }
            // considers move if human
            else if (!pTurn.isMoving() && board.getAvailablePiecesToMove().contains(tileClicked.getTileCoord()))
            {
                board.considerMove(tileClicked.getTileCoord());
            }
        }
        shortClick = false;
    }
}
