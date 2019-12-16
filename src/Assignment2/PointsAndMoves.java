package Assignment2;

/*
 * PointsAndMoves's purpose is mainly used within the AI's decision making, creating coordinate origin, destination,
 * and value of moves.
 * Comparable has been implemented to allow for natural sorting in TreeSet collections
 */
class PointsAndMoves implements Comparable<PointsAndMoves>{
    private Coord origin; // coorrdinate origin of move
    private Coord destination; // coordinate destination of move
    private int score; // score

    // default constructor
    PointsAndMoves(Coord origin, Coord destination)
    {
        this.origin = origin;
        this.destination = destination;
    }

    // rest are basic set/get/overrid methods
    Coord getOrigin() {
        return origin;
    }

    Coord getDestination() {
        return destination;
    }

    void setScore(int score) {
        this.score = score;
    }

    int getScore() {
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
