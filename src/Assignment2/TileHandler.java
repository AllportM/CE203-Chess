package Assignment2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Assignment2.Team.*;
public class TileHandler implements MouseListener {
    Board board;
    boolean shortClick = false;

    TileHandler(Board board)
    {
        this.board = board;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Team moveStatus = board.getMoveStatus();
        Tile tileEntered = ((Tile)e.getComponent());
        switch (moveStatus)
        {
            case P1TURN:
                if (tileEntered.isOccupied() && board.getP1().hasPiece(tileEntered.getTilePiece())
                        && !board.getP1().getKing().isUnderThreat())
                {
                    tileEntered.setColourMovePiece();
                    tileEntered.repaint();
                }
                else if (tileEntered.isOccupied() && board.getP1().hasPiece(tileEntered.getTilePiece())
                        && board.getP1().getKing().isUnderThreat()
                        && (tileEntered.getTilePiece() == board.getP1().getKing() || tileEntered.getTilePiece().isProtector()))
                {
                    tileEntered.setColourMovePiece();
                    tileEntered.repaint();
                }
                break;
            case P2TURN:
                if (tileEntered.isOccupied() && board.getP2().hasPiece(tileEntered.getTilePiece())
                        && !board.getP2().getKing().isUnderThreat())
                {
                    tileEntered.setColourMovePiece();
                    tileEntered.repaint();
                }
                else if (tileEntered.isOccupied() && board.getP2().hasPiece(tileEntered.getTilePiece())
                        && board.getP2().getKing().isUnderThreat()
                        && (tileEntered.getTilePiece() == board.getP2().getKing() || tileEntered.getTilePiece().isProtector()))
                {
                    tileEntered.setColourMovePiece();
                    tileEntered.repaint();
                }
                break;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Team moveStatus = board.getMoveStatus();
        Tile tileExited = ((Tile)e.getComponent());
        if (moveStatus != MOVING && tileExited.getToPaint() != tileExited.getDefaultCol())
        {
            tileExited.setColourDefault();
            tileExited.repaint();
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
            System.out.println("shortClick");
            shortClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (shortClick)
        {
            System.out.println("ClickSent");
            sendClick(e);
            shortClick = !shortClick;
        }
        if (board.getWinner() != null)
        {
            System.out.println("Congratulations " + board.getWinner());
        }
    }

    private void sendClick(MouseEvent e)
    {
        Team moveStatus = board.getMoveStatus();
        Tile tileClicked = ((Tile)e.getComponent());
        switch (moveStatus)
        {
            case P1TURN:
                if (tileClicked.isOccupied() && board.getP1().hasPiece(tileClicked.getTilePiece())) {
                    board.considerMove(tileClicked);
                    tileClicked.setColourMoving();
                }
                break;
            case P2TURN:
                if (tileClicked.isOccupied() && board.getP2().hasPiece(tileClicked.getTilePiece())) {
                    board.considerMove(tileClicked);
                    tileClicked.setColourMoving();
                }
                break;
            case MOVING:
                if (board.getPotentialMoves().contains(tileClicked.getTileCoord()))
                {
                    board.makeMove(tileClicked);
                }
                break;
        }
    }
}
