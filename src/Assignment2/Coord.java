package Assignment2;

public class Coord implements Comparable<Coord>{
    int x, y;
    public Coord (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "PosX= " + x + ", Pos Y = " + y;
    }

    @Override
    public boolean equals(Object o)
    {
        return (o instanceof Coord && ((Coord) o).x == this.x && ((Coord) o).y == this.y);
    }

    @Override
    public int hashCode()
    {
        return ((Integer) x).hashCode() + ((Integer) y).hashCode();
    }

    @Override
    public int compareTo(Coord o) {
        return (this.y > y)? 1: (this.y == o.y && this.x > o.x)? 1: (this.x < o.x)? -1: 0;
    }
}
