package Assignment2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

/*
 * MyKeyListener's purpose is to actuate the boards considerMove and makeMove methods dependant upon the
 * keys pressed, which in turn displays available moves or makes the move. Arrow keys are used to cycle through
 * the players available moves, enter to make the moves/consider them, and escape to back out of a move
 */
class MyKeyListener extends KeyAdapter {

    private Board board; // reference to the active board
    private ArrayList<Coord> moves; // stores available moves to be cycled through
    Integer index; // index position of where in the move list the player is, when index is null the move
                    // list is populated

    // the remaining members are static variables for keyboard values
    private static final int UP = 38;
    private static final int RIGHT = 39;
    private static final int DOWN = 40;
    private static final int LEFT = 37;
    static final int ENTER = 10;
    static final int ESCAPE = 27;

    // default constructor initializing board reference
    public MyKeyListener(Board board)
    {
        this.board = board;
    }

    // overrides the main keyPressed method to actuate methods
    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        int pressed = e.getExtendedKeyCode();
        if (board.playersTurn instanceof HumanPlayer)
        {
            switch (pressed) {
                // up and left to cycle backwards through moves
                case UP:
                case LEFT:
                    // selecting a piece to move
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
                            index = (index < 0) ? moves.size() - 1 : index;
                            board.clearColouredTiles();
                            board.getTile(moves.get(index)).setColourMovePiece();
                        }

                    } else
                    { // cycles through the pieces available moves
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
                            index = (index < 0) ? moves.size() - 1 : index;
                            Tile move = board.getTile(moves.get(index));
                            move.setColourMovePiece();
                            board.getUi().repaint();
                        }
                    }
                    break;
                    // down and right cycle forward through the moves list
                case DOWN:
                case RIGHT:
                    // cycles through pieces a player can move forward
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
                    { // cycles through pieces moves
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
                    // actuates either makeMove or considerMove
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
                    // sets index to null so moves can be refreshed, and makes a move to the pieces origin (a null move)
                    index = null;
                    board.clearColouredTiles();
                    if (board.getPlayersTurn().isMoving()) board.makeMove(board.getMovingTile().getTileCoord());
                    break;
            }
        }
    }

    // setPlayersTurnMoves  simply populates the moves with a pieces available moves
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
