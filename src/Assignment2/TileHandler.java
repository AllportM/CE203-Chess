package Assignment2;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import static Assignment2.Team.*;
public class TileHandler implements MouseListener, KeyListener {
    Board board;
    boolean shortClick = false;
    boolean entered = false;
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
        if (!board.isGameOver() && !player.isMoving() && player.isHuman() && tileEntered.isOccupied()
                && ((HumanPlayer) player).hasPiece(tileEntered)
                && player.getPieceMoves(tileEntered.getTilePiece()).size() > 0)
        {
            tileEntered.setColourMovePiece();
            board.getUi().repaint();
            entered = true;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Tile tileExited = ((Tile)e.getComponent());
        if (!board.isGameOver() && !board.getPlayersTurn().isMoving())
        {
            tileExited.setColourDefault();
            board.getUi().repaint();
            if (entered) entered = false;
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
        if (shortClick && entered && pTurn.isHuman() && !board.isGameOver())
        {
            // makes move if human
            if (pTurn.isMoving() && ((board.getMovingPiecesMoves().contains(tileClicked.getTileCoord()))
                || tileClicked.getTileCoord().equals(board.getMovingTile().getTileCoord())))
            {
                board.clearColouredTiles();
                entered = false;
                pTurn.board.makeMove(tileClicked.getTileCoord());
                board.getUi().repaint();
                // ai makes move if its turn
                if (board.getP2().getPlayerType() == AI && board.getPlayersTurn().getTeam() == P2 && !board.isGameOver())
                {
                    ((AIPlayer) board.getP2()).makeMove();
                    board.getUi().repaint();
                }
            }
            // considers move if human
            else if (!pTurn.isMoving())

            {
                board.considerMove(tileClicked.getTileCoord());
            }
        }
        shortClick = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyCode());
        System.out.println("Hey");
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
