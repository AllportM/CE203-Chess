package Assignment2;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Assignment2.Team.*;

/*
 * The piece abstract class's purpose is to define generalised methods and variables applicable to all chess pieces
 */
abstract class Piece
{
    Coord position; // x, y coordinate of the pieces current position
    final Team teamPiece; // the team the piece belongs to, indicative of what player, P1 or P2
    final Shape drawing; // an assortment of graphic drawings for specific pieces, used in paintComponents
    Color col; // WHITE or BLACK, the colour the piece is
    boolean firstMove;
    String stringCol;

    /*
     * Default constructor, initiates coordinates and team
     *
     * @params
     *      Coord place, the x, y coordinates the piece is initiated to
     *      Team team, the team to which the piece belongs (WHITE, or BLACK)
     */
    Piece (Coord place, Team team, Team teamCol)
    {
        position = place;
        teamPiece = team;
        if (teamCol == WHITE)
        {
            col = Color.white;
            stringCol = "WHITE";
        }
        else
        {
            col = Color.black;
            stringCol = "BLACK";
        }
        drawing = setDrawing();
        firstMove = true;
    }

    // copy constructor
    Piece(Piece piece)
    {
        position = new Coord(piece.getPosition().x, piece.getPosition().y);
        teamPiece = piece.teamPiece;
        drawing = piece.drawing;
        col = piece.col;
        stringCol = piece.stringCol;
        firstMove = piece.firstMove;
    }

    abstract HashSet<Coord> getValidMoves(Board board);
    /*
     * setDrawing's purpose is to compile an assortment of Shapes and add them to the ComplexShape group to be drawn
     * via paintComponent's Graphic object
     */
    abstract Shape setDrawing();

    /*
     * validMove's purpose is to check whether a given move is within the bounds of the board sizes, i.e if a piece
     * is to be moved from the 8th column, it cannot go further into the 9th column, or to check whether the position
     * moving to is occupied by a piece of the same team
     *
     * @params
     *      Board board, the current board state
     *      int x, x position being cheked
     *      int y, y position being checked
     * @return
     *      boolean, true if valid false otherwise
     */
    boolean validMove(Board board, int x, int y)
    {
        if (x < 0 || x > 7 || y < 0 || y > 7) return false;
        if (board.isOccupied(x, y) && board.getTile(x, y).getTilePiece().teamPiece == teamPiece) return false;
        else return true;
    }

    /*
     * setCoord's purpose is to change the coordinate the piece belongs, used on movePiece in game
     *
     * @params
     *      Coord place, coordinate to be updated to
     */
    void setCoord(Coord place)
    {
        position = new Coord(place.x, place.y);
    }


    Coord getPosition()
    {
        return position;
    }

    boolean isFirstMove()
    {
        return firstMove;
    }

    void setFirstMove()
    {
        firstMove = false;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Piece)) return false;
        Piece piece =  (Piece) o;
        if (piece.getPosition().equals(this.getPosition()) && piece.col == this.col) return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return (position.hashCode() + col.hashCode());
    }

    // will be overriding toString for debugging purposes
//    @Override
//    public abstract String toString();
}

/*
 * Pawn class's purpose is to implement Pawns specific methods
 */
class Pawn extends Piece
{
    /*
     * Default constructor, calling super's
     */
    Pawn (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
    }

    Pawn(Pawn piece)
    {
        super(piece);
    }

    @Override
    public HashSet<Coord> getValidMoves(Board board)
    {
        HashSet<Coord> moves = new HashSet<>();
        int x = position.x;
        int y = position.y;
        int singleMove = (teamPiece == P1) ? -1 : 1; // changes if team == black or white
        int doubleMove = (singleMove < 0) ? -2 : +2;
        if (setMoves(moves, board, x, y, singleMove) && firstMove)
            setMoves(moves, board, x, y, doubleMove);
        return moves;
    }

    boolean setMoves(Set<Coord> moves, Board board, int x, int y, int move)
    {
        switch (move)
        {
            case 1:
            case -1:
                // attack left
                if (x != 0 && validMove(board, x-1, y + move)
                        && board.getTile(x-1, y+move).isOccupied()
                        && board.getTile(x-1, y+move).getTilePiece().teamPiece != teamPiece)
                    moves.add(new Coord(x-1, y+move));
                // attack right
                if (x != 7 && validMove(board, x+1, y + move)
                        && board.getTile(x+1, y+move).isOccupied()
                        && board.getTile(x+1, y+move).getTilePiece().teamPiece != teamPiece)
                    moves.add(new Coord(x+1, y+move));
                // move forward
                if (validMove(board, x, y + move) && !board.getTile(x, y + move).isOccupied())
                {
                    moves.add(new Coord(x, y + move));
                    return true;
                }
                break;
            case 2:
            case -2:
                if (validMove(board, x, y + move) && !board.getTile(x, y+move).isOccupied())
                    moves.add(new Coord(x, y+move));
                break;
        }
        return false;
    }

    /*
     * Draws
     */
    Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(col));
        int[] polyX = {45, 35, 65, 55};
        int[] polyY = {33, 75, 75, 33};
        cp.addToShape(new Poly(polyX, polyY,  col));
        cp.addToShape(new Circle(50, 30, 12, col));
        return cp;
    }

    // Overriding to string for debugging purposes
    @Override
    public String toString()
    {
        return "P" + stringCol;
    }
}

class Castle extends Piece
{
    /*
     * Default constructor, calling super's
     */
    Castle (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
    }

    Castle(Castle piece)
    {
        super(piece);
        firstMove = piece.firstMove;
    }

    /*
     * Draws
     */
    Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(col));
        cp.addToShape(new Rectangle(25, 75, 70, 76,  col));
        int[] trapX = {30, 25, 74, 70};
        int[] trapY = {65, 70, 70, 65};
        cp.addToShape(new Poly(trapX, trapY, col)); // lower base trap
        cp.addToShape(new Rectangle(30, 70, 35, 66, col)); // body
        int[] trapX2 = {30, 25, 74, 70};
        int[] trapY2 = {35, 30, 30, 35};
        cp.addToShape(new Poly(trapX2, trapY2, col)); // upper base trap
        cp.addToShape(new Rectangle(25, 75, 20, 31, col));
        cp.addToShape(new Rectangle(25, 40, 10, 21, col));
        cp.addToShape(new Rectangle(43, 57, 10, 21, col));
        cp.addToShape(new Rectangle(60, 75, 10, 21, col));
        return cp;
    }

    @Override
    HashSet<Coord> getValidMoves(Board board)
    {
        HashSet<Coord> moves = new HashSet<>();
        //forward moves
        int x = position.x;
        int y = position.y + 1;
        setMoves(moves, board, x, y, 0, 1);
        // backward moves
        y = position.y - 1;
        setMoves(moves, board, x, y, 0, -1);
        //left moves
        y = position.y;
        x = position.x - 1;
        setMoves(moves, board, x, y, -1, 0);
        //right moves
        x = position.x + 1;
        setMoves(moves, board, x, y, 1, 0);
        return moves;
    }

    private void setMoves (Set<Coord> moves, Board board, int x, int y, int xDir, int yDir)
    {
        while (validMove(board, x, y))
        {
            Coord move = new Coord(x, y);
            if (board.getTile(x,y).isOccupied() && board.getTile(x, y).getTilePiece().teamPiece != teamPiece)
            {
                moves.add(move);
                return;
            }
            moves.add(move);
            switch (xDir)
            {
                case 1:
                    x++;
                    break;
                case 0:
                    break;
                case -1:
                    x--;
                    break;
            }
            switch (yDir)
            {
                case 1:
                    y++;
                    break;
                case -1:
                    y--;
                    break;
                case 0:
                    break;
            }
        }
    }

    @Override
    public String toString()
    {
        return "C" + stringCol;
    }
}

class Horse extends Piece
{
    Horse (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
    }

    Horse(Piece piece)
    {
        super(piece);
    }

    public Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(this.col));
        cp.addToShape(new Rectangle(25, 75, 70, 76,  col));
        int[] baseTrapX = {30, 25, 74, 70};
        int[] baseTrapY = {65, 70, 70, 65};
        cp.addToShape(new Poly(baseTrapX, baseTrapY, col));
        int[] bodX = {45, 35, 64, 70};
        int[] bodY = {32, 65, 65, 30};
        cp.addToShape(new Poly(bodX, bodY, col)); // body
        int[] headX = {30, 30, 70, 70};
        int[] headY = {25, 45, 35, 10};
        cp.addToShape(new Poly(headX, headY, col)); // head
        int[] earX = {60, 55, 65};
        int[] earY = {8, 17, 17};
        cp.addToShape(new Triangle(earX, earY, col));
        return cp;
    }

    @Override
    public HashSet<Coord> getValidMoves(Board board) {
        HashSet<Coord> moves = new HashSet<>();
        // forward 2 right 1
        int x = position.x + 1;
        int y = position.y + 2;
        setMoves(moves, board, x, y);
        // forward 2 left 1
        x = position.x - 1;
        setMoves(moves, board, x, y);
        // backward 2 left 1
        y = position.y - 2;
        setMoves(moves, board, x, y);
        // backward 2 right 1
        x = position.x + 1;
        setMoves(moves, board, x, y);
        // forward 1 left 2
        y = position.y + 1;
        x = position.x - 2;
        setMoves(moves, board, x, y);
        // forward 1 right 2
        x = position.x + 2;
        setMoves(moves, board, x, y);
        //backward 1 right 2
        y = position.y - 1;
        setMoves(moves, board, x, y);
        // backward 1 left 2
        x = position.x - 2;
        setMoves(moves, board, x, y);
        return moves;
    }

    private void setMoves(Set<Coord> moves, Board board, int x, int y)
    {
        if (validMove(board, x, y)) moves.add(new Coord(x, y));
    }

    @Override
    public String toString() {
        return "H" + stringCol;
    }
}

class Bishop extends Piece
{
    Bishop (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
    }

    Bishop(Piece piece)
    {
        super(piece);
    }

    /*
     * Draws
     */
    public Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(this.col));
        int[] bodyX = {45, 35, 65 ,55};
        int[] bodyY = {36, 70, 70, 36};
        cp.addToShape(new Poly(bodyX, bodyY, col));
        cp.addToShape(new Rectangle(25, 75, 70, 76,  col));
        int[] baseTrapX = {30, 25, 74, 70};
        int[] baseTrapY = {65, 70, 70, 65};
        cp.addToShape(new Poly(baseTrapX, baseTrapY, col));
        cp.addToShape(new Oval(40, 30, 20, 10,  col)); // head
        cp.addToShape(new Oval(42, 7, 16, 25,  col)); // head
        return cp;
    }

    @Override
    HashSet<Coord> getValidMoves(Board board) {
        HashSet<Coord> moves = new HashSet<>();
        //forward right
        int x = position.x +1;
        int y = position.y +1;
        setMoves(moves, board, x, y, 1, 1);
        // forward left
        x = position.x -1;
        setMoves(moves, board, x, y, -1, 1);
        // backward left
        y = position.y -1;
        setMoves(moves, board, x, y, -1, -1);
        // backward right
        x = position.x +1;
        setMoves(moves, board, x, y, 1, -1);
        return moves;
    }

    @Override
    public String toString() {
        return "B" + stringCol;
    }

    private void setMoves(Set<Coord> moves, Board board, int x, int y, int xDir, int yDir)
    {
        while(validMove(board, x, y))
        {
            Coord move = new Coord(x, y);
            if (board.getTile(x, y).isOccupied() && board.getTile(x, y).getTilePiece().teamPiece != teamPiece)
            {
                moves.add(move);
                return;
            }
            moves.add(move);
            switch (xDir)
            {
                case 1:
                    x++;
                    break;
                case -1:
                    x--;
                    break;
            }
            switch (yDir)
            {
                case 1:
                    y++;
                    break;
                case -1:
                    y--;
                    break;
            }
        }
    }
}

class Queen extends Piece
{
    Queen (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
    }

    Queen (Piece piece)
    {
        super(piece);
    }

    /*
     * Draws
     */
    public Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(this.col));
        int[] bodyX = {40, 35, 65 ,60};
        int[] bodyY = {36, 70, 70, 36};
        cp.addToShape(new Poly(bodyX, bodyY, col));
        cp.addToShape(new Oval(25, 65, 50, 11, col)); // base circle
        cp.addToShape(new Rectangle(45, 55, 20, 35, col)); // collar
        cp.addToShape(new Oval(35, 30, 30, 9, col)); // neck
        cp.addToShape(new Oval(40, 7, 20, 20,  col)); // head
        return cp;
    }

    @Override
    public HashSet<Coord> getValidMoves(Board board) {
        HashSet<Coord> moves = new HashSet<>();
        //forward right
        int x = position.x +1;
        int y = position.y +1;
        setMoves(moves, board, x, y, 1, 1);
        // forward left
        x = position.x -1;
        setMoves(moves, board, x, y, -1, 1);
        // backward left
        y = position.y -1;
        setMoves(moves, board, x, y, -1, -1);
        // backward right
        x = position.x +1;
        setMoves(moves, board, x, y, 1, -1);
        //forward moves
        x = position.x;
        y = position.y + 1;
        setMoves(moves, board, x, y, 0, 1);
        // backward moves
        y = position.y - 1;
        setMoves(moves, board, x, y, 0, -1);
        //left moves
        y = position.y;
        x = position.x - 1;
        setMoves(moves, board, x, y, -1, 0);
        //right moves
        x = position.x + 1;
        setMoves(moves, board, x, y, 1, 0);
        return moves;
    }

    private void setMoves(Set<Coord> moves, Board board, int x, int y, int xDir, int yDir)
    {
        while (validMove(board, x, y)) {
            Coord move = new Coord(x, y);
            if (board.getTile(x, y).isOccupied() && board.getTile(x, y).getTilePiece().teamPiece != teamPiece) {
                moves.add(move);
                return;
            }
            moves.add(move);
            switch (xDir) {
                case 1:
                    x++;
                    break;
                case -1:
                    x--;
                    break;
            }
            switch (yDir) {
                case 1:
                    y++;
                    break;
                case -1:
                    y--;
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "Q" + stringCol;
    }
}

class King extends Piece
{

    private boolean underThreat;

    King (Coord place, Team team, Team teamCol)
    {
        super(place, team, teamCol);
        underThreat = false;
    }

    King(King piece)
    {
        super(piece);
        underThreat = piece.underThreat;
    }

    /*
     * Draws
     */
    public Shape setDrawing()
    {
        ComplexShape cp = new ComplexShape();
        cp.addToShape(new Base(this.col));
        int[] bodyX = {40, 35, 65 ,60};
        int[] bodyY = {36, 70, 70, 36};
        cp.addToShape(new Poly(bodyX, bodyY, col));
        cp.addToShape(new Oval(25, 65, 50, 11, col)); // base circle
        cp.addToShape(new Rectangle(45, 55, 20, 35, col)); // collar
        cp.addToShape(new Oval(35, 30, 30, 9, col)); // neck
        cp.addToShape(new Oval(40, 7, 20, 20,  col)); // head
        int[] crownX = {50, 45, 50, 55};
        int[] crownY = {0, 10, 20, 10};
        cp.addToShape(new Poly(crownX, crownY, col));
        return cp;
    }

    @Override
    public HashSet<Coord> getValidMoves(Board board)
    {
        HashSet<Coord> moves = new HashSet<>();
        //forward
        int x = position.x;
        int y = position.y + 1;
        setMove(moves, board, x, y);
        //forward left
        x = position.x - 1;
        setMove(moves, board, x, y);
        //forward right
        x = position.x + 1;
        setMove(moves, board, x, y);
        //backward right
        y = position.y - 1;
        setMove(moves, board, x, y);
        //backward left
        x = position.x - 1;
        setMove(moves, board, x, y);
        //backward
        x = position.x;
        setMove(moves, board, x, y);
        //Left
        y = position.y;
        x = position.x - 1;
        setMove(moves, board, x, y);
        //right
        x = position.x + 1;
        setMove(moves, board, x, y);
        return moves;
    }

    private void setMove(Set<Coord> moves, Board board, int x, int y)
    {
        if (validMove(board, x, y) && (!board.isOccupied(x, y)
                || board.getTile(x, y).getTilePiece().teamPiece != teamPiece))
            moves.add(new Coord(x, y));
    }


    void setUnderThreat(boolean huh)
    {
        underThreat = huh;
    }


    boolean isUnderThreat()
    {
        return underThreat;
    }


    @Override
    public String toString() {
        return "K" + stringCol;
    }
}