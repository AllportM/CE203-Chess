package Assignment2;

import java.util.Objects;

/*
 * Coord's purpose is to merely contain x and y positional values
 */
public class Coord implements Comparable<Coord>{
    int x, y;// coordinates

    /*
     * default constructor, sets the x/y values
     *
     * @params
     *      x, integer x position
     *      y, integer y position
     */
    public Coord (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /*
     * Constructor taking a Coord as parameter
     *
     * @param
     *      pos, Coordinate containing x/y values
     */
    public Coord(Coord pos)
    {
        x = pos.x;
        y = pos.y;
    }

    // overrides toString method
    @Override
    public String toString()
    {
        return "PosX= " + x + ", Pos Y = " + y;
    }

    // overrides equals for equality comparisons
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Coord)) return false;
        if (((Coord) o).x == this.x && ((Coord) o).y == this.y) return true;
        return false;
    }

    // overrides hashCodes for equality in HashSets
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    // overrides comparisons for use in sorting of TreeSets
    @Override
    public int compareTo(Coord o) {
        return (this.y > y)? 1: (this.y == o.y && this.x > o.x)? 1: (this.x < o.x)? -1: 0;
    }
}
