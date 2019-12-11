package Assignment2;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import static Assignment2.Team.*;
public class TileHandler implements MouseListener{
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
        Player player = board.getPlayersTurn();
        Tile tileEntered = ((Tile)e.getComponent());
        if (!player.isMoving() && player.isHuman() && tileEntered.isOccupied()
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
        if (!board.getPlayersTurn().isMoving())
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
    public void mousePressed(MouseEvent e) {
        if (!shortClick)
        {
            shortClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Player pTurn = board.getPlayersTurn();
        Tile tileClicked = ((Tile)e.getComponent());
        if (shortClick && entered && pTurn.isHuman())
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
                if (board.getP2().getPlayerType() == AI && board.getPlayersTurn().getTeam() == P2)
                {
                    board.getP2().makeMove(tileClicked);
                    board.getUi().repaint();
                    board.getUi().revalidate();
                }
            }
            // considers move if human
            else if (!pTurn.isMoving())

            {
                board.considerMove(tileClicked.getTileCoord());
            }
        }
        if (board.isGameOver())
        {
            System.out.println("Congratulations " + ((board.getWinner() == board.getP1()? "Player 1":
                    board.getWinner() == board.getP2()? "Player 2":", you reached a StaleMate")));
        }
        shortClick = false;
    }

}
