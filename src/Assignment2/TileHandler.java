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
    public void mouseEntered(MouseEvent e)
    {
        Player player = board.getPlayersTurn();
        Tile tileEntered = ((Tile)e.getComponent());
        if (!player.isMoving())
        {
            if (tileEntered.isOccupied() && player.hasPiece(tileEntered.getTilePiece())
                    && !player.getKing().isUnderThreat()) {
                tileEntered.setColourMovePiece();
                tileEntered.repaint();
            } else if (tileEntered.isOccupied() && player.hasPiece(tileEntered.getTilePiece())
                    && player.getKing().isUnderThreat()
                    && (tileEntered.getTilePiece() == player.getKing() || tileEntered.getTilePiece().isProtector())) {
                tileEntered.setColourMovePiece();
                tileEntered.repaint();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Player player = board.getPlayersTurn();
        Tile tileExited = ((Tile)e.getComponent());
        if (!player.isMoving() && tileExited.getToPaint() != tileExited.getDefaultCol())
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
        if (board.isGameOver())
        {
            System.out.println("Congratulations " + ((board.getWinner() == board.getP1()? "Player 1":
                    board.getWinner() == board.getP2()? "Player 2":", you reached a StaleMate")));
        }
    }

    private void sendClick(MouseEvent e)
    {
        Player player = board.getPlayersTurn();
        Tile tileClicked = ((Tile)e.getComponent());
        if (!player.isMoving() && tileClicked.isOccupied() && player.hasPiece(tileClicked.getTilePiece()))
        {
            board.considerMove(tileClicked);
            board.getUi().repaint();
            tileClicked.setColourMoving();
        }
        else
        {
            board.makeMove(tileClicked);
            board.getUi().repaint();
            if (board.getPlayersTurn().getPlayerType() == AI)
            {
                board.getP2().getAiPlayer().callMiniMax(board);
                PointsAndMoves move = board.getP2().getAiPlayer().getBestMove();
                board.considerMove(board.getTile(move.getOrigin().x, move.getOrigin().y));
                board.makeMove(board.getTile(move.getDestination().x, move.getDestination().y));
            }
        }
    }
}
