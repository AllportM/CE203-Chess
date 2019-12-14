package Assignment2;

import java.util.Objects;

public class Coord implements Comparable<Coord>{
    int x, y;
    public Coord (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord pos)
    {
        x = pos.x;
        y = pos.y;
    }

    @Override
    public String toString()
    {
        return "PosX= " + x + ", Pos Y = " + y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Coord)) return false;
        if (((Coord) o).x == this.x && ((Coord) o).y == this.y) return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }


    @Override
    public int compareTo(Coord o) {
        return (this.y > y)? 1: (this.y == o.y && this.x > o.x)? 1: (this.x < o.x)? -1: 0;
    }
}
