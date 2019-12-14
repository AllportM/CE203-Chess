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
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        int pressed = e.getExtendedKeyCode();
        if (board.playersTurn instanceof HumanPlayer)
        {
            switch (pressed) {
                case UP:
                case LEFT:
                    if (!board.getPlayersTurn().isMoving())
                    {
                        if (index == null)
                        {
                            moves = new ArrayList<>();
                            moves.addAll(board.getAvailablePiecesToMove());
                            Collections.sort(moves);
                            index = moves.size() - 1;
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

                    } else {
                        if (index == null)
                        {
                            moves = new ArrayList<>();
                            moves.addAll(board.getMovingPiecesMoves());
                            Collections.sort(moves);
                            index = moves.size() - 1;
                            Tile move = board.getTile(moves.get(index));
                            move.setColourMovePiece();
                            board.getUi().repaint();
                        }
                        else
                        {
                            board.getTile(moves.get(index)).setColourMoving();
                            index--;
                            forward = false;
                            index = (index < 0) ? moves.size() - 1 : index;
                            Tile move = board.getTile(moves.get(index));
                            move.setColourMovePiece();
                            board.getUi().repaint();
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
                            board.clearColouredTiles();
                            board.getTile(moves.get(index)).setColourMovePiece();
                        }
                        else
                        {
                            index++;
                            index = (index > moves.size() - 1) ? 0 : index;
                            board.clearColouredTiles();
                            board.getTile(moves.get(index)).setColourMovePiece();
                        }
                    }
                    else
                    {
                        if (index == null)
                        {
                            setPlayersTurnMoves();
                        } else
                        {
                            board.getTile(moves.get(index)).setColourMoving();
                            index++;
                            index = (index > moves.size() - 1) ? 0 : index;
                            Tile move = board.getTile(moves.get(index));
                            move.setColourMovePiece();
                            board.getUi().repaint();
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
                            setPlayersTurnMoves();
                        }
                    }
                    break;
                case ESCAPE:
                    index = null;
                    board.clearColouredTiles();
                    if (board.getPlayersTurn().isMoving()) board.makeMove(board.getMovingTile().getTileCoord());
                    break;
            }
        }
    }

    private void setPlayersTurnMoves() {
        moves = new ArrayList<>();
        moves.addAll(board.getMovingPiecesMoves());
        Collections.sort(moves);
        index = 0;
        Tile move = board.getTile(moves.get(index));
        move.setColourMovePiece();
        board.getUi().repaint();
    }

    void clearIndex()
    {
        index = null;
    }
}
