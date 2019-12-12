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
        // Average chess game takes approx 40 moves [1], on the basis of 20 seconds per move and 20,000 points for this
        // duration 8000000/(20*20) == 20,000 points. so 8000000/time taken = score. *1 for stalemate, * 2 for win
        // [1]"Chess Statistics", Chessgames.com, 2019. [Online].
        // Available: https://www.chessgames.com/chessstats.html. [Accessed: 12- Dec- 2019].
        try {
            score = (8000000 / (time / 1000)) * winner;
        }catch(ArithmeticException e){score = (8000000 / (20*20)) * winner;}
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
        return this.score - o.score;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(score, time, name);
    }

    @Override
    public boolean equals(Object o)
    {
        PlayerScore newO = (o instanceof PlayerScore)? (PlayerScore) o: null;
        return (newO != null && newO.score == score && newO.name.equals(name) && newO.score == score);
    }
}
