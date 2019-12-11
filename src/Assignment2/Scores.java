package Assignment2;

import java.util.Objects;

class PlayerScore implements Comparable<PlayerScore>{
    private int score;
    private String name;
    private int time;

    public PlayerScore(String entry)
    {
        String[] entity= entry.split(" ");
        name = entity[0];
        score = Integer.parseInt(entity[1]);
        time = Integer.parseInt(entity[2]);
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return String.valueOf(time);
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
