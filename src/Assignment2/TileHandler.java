package Assignment2;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import static Assignment2.Team.*;
public class TileHandler implements MouseListener {
    Board board;
    boolean shortClick = false;
    Piece movinePiece;

    TileHandler(Board board)
    {
        this.board = board;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        board.getUi().keyListener.clearIndex();
        Player player = board.getPlayersTurn();
        Tile tileEntered = ((Tile)e.getComponent());
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
//        if (!shortClick)
//        {
//            System.out.println("MouseClick");
//            sendClick(e);
//        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!board.isGameOver() && !shortClick)
        {
            shortClick = true;
        }
    }

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
