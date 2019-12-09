package Assignment2;

class PointsAndMoves implements Comparable<PointsAndMoves>{
    private Coord origin;
    private Coord destination;
    private int score;

    public PointsAndMoves(Coord origin, Coord destination)
    {
        this.origin = origin;
        this.destination = destination;
    }

    public Coord getOrigin() {
        return origin;
    }

    public Coord getDestination() {
        return destination;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(PointsAndMoves other)
    {
        return other.score - score; // for descending order
    }

    @Override
    public int hashCode()
    {
        return ((Integer) origin.x).hashCode() + ((Integer) origin.y).hashCode()
                + ((Integer) destination.x).hashCode() + ((Integer) destination.y).hashCode()
                + ((Integer) score).hashCode();
    }

    @Override
    public String toString()
    {
        return "PointsAndScores\nMy origin is:- " + origin.x + ", " + origin.y
                + "\nMy Destination is:- " + destination.x + ", " + destination.y;
    }

}
