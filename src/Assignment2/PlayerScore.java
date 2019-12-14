package Assignment2;

import java.util.Objects;
import static Assignment2.Team.*;

class PlayerScore implements Comparable<PlayerScore>{
    private int score;
    private String name;
    private int time;

    PlayerScore(String entry)
    {
        String[] entity= entry.split(" ");
        name = entity[0];
        score = Integer.parseInt(entity[1]);
        time = Integer.parseInt(entity[2]);
    }

    public PlayerScore(Player p1)
    {
        name = p1.pName;
        time = ((HumanPlayer) p1).getSecondsTotal();
        int winner = (((HumanPlayer) p1).getWinStatus() == WINNER)? 2:
                ((HumanPlayer) p1).getWinStatus() == STALEMATE? 1: 0;
        // Average chess game takes approx 40 moves [1], on the basis of 10 seconds per move and 2,000 points for this
        // duration 400000/(20*10) == 2,000 points. so 400000/time taken = score. *1 for stalemate, * 2 for win
        // [1]"Chess Statistics", Chessgames.com, 2019. [Online].
        // Available: https://www.chessgames.com/chessstats.html. [Accessed: 12- Dec- 2019].
        try {
            score = (400000 / (time / 1000)) * winner;
        }catch(ArithmeticException e){score = (400000 / (20*10)) * winner;}
    }

    String getScore() {
        return String.valueOf(score);
    }

    String getName() {
        return name;
    }

    int getTime() {
        return time;
    }

    @Override
    public int compareTo(PlayerScore o)
    {
        return (this.score - o.score > 0)? 1: (this.score - o.score) < 0? -1: this.name.compareTo(o.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, score, time);
    }

    @Override
    public boolean equals(Object o)
    {
        return (o instanceof PlayerScore && ((PlayerScore) o).name.equals(name) && ((PlayerScore) o).score == score
                && ((PlayerScore) o).time==time);
    }

    @Override
    public String toString()
    {
        return name + " " + score + " " + time;
    }
}
