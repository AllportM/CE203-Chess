package Assignment2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

class MyKeyListener extends KeyAdapter {

    private Board board;
    private ArrayList<Coord> moves;
    Integer index;
    private boolean forward = false;

    private static final int UP = 38;
    private static final int RIGHT = 39;
    private static final int DOWN = 40;
    private static final int LEFT = 37;
    static final int ENTER = 10;
    static final int ESCAPE = 27;

    public MyKeyListener(Board board)
    {
        this.board = board;
        System.out.println(board);
        System.out.println(board.getPlayersTurn());
        System.out.println(board.getPlayersTurn().isMoving());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int pressed = e.getExtendedKeyCode();
        switch(pressed)
        {
            case UP:
            case LEFT:
                if (!board.getPlayersTurn().isMoving())
                {
                    if (index == null)
                    {
                        moves = new ArrayList<>();
                        moves.addAll(board.getAvailablePiecesToMove());
                        Collections.sort(moves);
                        index = moves.size() -1;
                        board.clearColouredTiles();
                        board.getTile(moves.get(index)).setColourMovePiece();
                    }
                    else
                    {
                        index--;
                        forward = false;
                        index = (index < 0) ? moves.size() - 1 : index;
                        board.clearColouredTiles();
                        board.getTile(moves.get(index)).setColourMovePiece();
                    }

                }
                else
                {
                    if (index == null)
                    {
                        moves = new ArrayList<>();
                        moves.addAll(board.getMovingPiecesMoves());
                        Collections.sort(moves);
                        forward = false;
                        index = moves.size() -1;
                        board.clearColouredTiles();
                        Tile move = board.getTile(moves.get(index));
                        if (move.isOccupied()) move.setColourHostile();
                        else move.setColourMoving();
                    }
                    else
                    {
                        index--;
                        forward = false;
                        index = (index < 0) ? moves.size() - 1 : index;
                        board.clearColouredTiles();
                        Tile move = board.getTile(moves.get(index));
                        if (move.isOccupied()) move.setColourHostile();
                        else move.setColourMoving();
                    }
                }
                break;
            case DOWN:
            case RIGHT:
                if (!board.getPlayersTurn().isMoving())
                {
                    if (index == null)
                    {
                        moves = new ArrayList<>();
                        moves.addAll(board.getAvailablePiecesToMove());
                        Collections.sort(moves);
                        index = 0;
                        forward = true;
                        board.clearColouredTiles();
                        board.getTile(moves.get(index)).setColourMovePiece();
                    }
                    else
                    {
                        index++;
                        forward = true;
                        index = (index > moves.size() - 1) ? 0 : index;
                        board.clearColouredTiles();
                        board.getTile(moves.get(index)).setColourMovePiece();
                    }
                }
                else
                {
                    if (index == null)
                    {
                        moves = new ArrayList<>();
                        moves.addAll(board.getMovingPiecesMoves());
                        Collections.sort(moves);
                        forward = true;
                        index = 0;
                        Tile move = board.getTile(moves.get(index));
                        if (move.isOccupied()) move.setColourHostile();
                        else move.setColourMoving();
                    }
                    else
                    {
                        if (!forward) index += 1;
                        index++;
                        forward = true;
                        index = (index > moves.size() - 1) ? 0 : index;
                        board.clearColouredTiles();
                        Tile move = board.getTile(moves.get(index));
                        if (move.isOccupied()) move.setColourHostile();
                        else move.setColourMoving();
                    }
                }
                break;
            case ENTER:
                if (!(index == null))
                {
                    if (board.playersTurn.isMoving())
                    {
                        board.makeMove(moves.get(index));
                        index = null;
                    }
                    else
                    {
                        board.considerMove(moves.get(index));
                        index = null;
                        board.clearColouredTiles();
                    }
                }
                break;
            case ESCAPE:
                index = null;
                break;
        }
    }

    void clearIndex()
    {
        index = null;
    }
}
