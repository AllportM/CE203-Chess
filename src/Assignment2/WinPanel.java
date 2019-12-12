package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import static Assignment2.Team.*;

public class WinPanel extends JPanel {
    private Tile tile = new Tile(new Coord(0,0));

    WinPanel(Board board)
    {
        ScoreHandler scores = new ScoreHandler("scores.txt");
        setPreferredSize(new Dimension(800, 800));
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        ComplexGBL gblGroup = new ComplexGBL();

        // generates a random chess piece from either the winners team, or player 1's team if stalemate
        int winner;
        if (board.getWinner() != null)
        {
            winner = (board.getWinner().getTeam() == Team.WHITE)? 1: 0;
        }
        else winner = (board.getP1().getTeam() == Team.WHITE)? 1: 0;
        Piece winnerPiece = PlayerWelcome.getPiece(winner);
        // creates tile for piece and sets constraints
        Tile tile = new Tile(new Coord(0,0));
        tile.setPiece(winnerPiece);
        gblGroup.addToList(new GBLLeaf(0, 0, 3, 1, new Insets(50, 0, 50, 0), tile));

        // adds gameover label
        String gOver = (((HumanPlayer) board.getP1()).getWinStatus() == WINNER)? "Congratulations! you Won!!!":
                ((HumanPlayer) board.getP1()).getWinStatus() == STALEMATE? "Congratulations, you reached a Stalemate":
                        "Sorry, you were beaten, better luck next time";
        JLabel gameover = new JLabel(gOver);
        gblGroup.addToList(new GBLLeaf(0, 1, 3, 1, gameover));

        JLabel pScore = new JLabel("Your score");
        gblGroup.addToList(new GBLLeaf(0, 2, 3, 1, new Insets(50, 0, 10, 0), pScore));

        // adds current player score to layout
        PlayerScore playerScore = new PlayerScore(board.getP1());
        JLabel name, time, score;
        name = new JLabel(playerScore.getName());
        time = new JLabel(getClockFormat(playerScore.getTime()));
        score = new JLabel(playerScore.getScore());
        gblGroup.addToList(new GBLLeaf(0, 3, name));
        gblGroup.addToList(new GBLLeaf(1, 3, time));
        gblGroup.addToList(new GBLLeaf(2, 3, score));
        JPanel inner = new JPanel();
        inner.setPreferredSize(new Dimension(400, 400));
        inner.setLayout(gbl);
        add(inner);
        scores.getScores().add(playerScore);

        // adds top 5 scores, if 5 scores exist
        JLabel topScores = new JLabel("Top 5 Scores");
        gblGroup.addToList(new GBLLeaf(0,4,3,1, new Insets(50,0,0,10), topScores));
        if (scores.getScores().size() > 0)
        {
            int topScoresCount = Math.min(scores.getScores().size(), 5);
            int itCount = 0;
            for(Iterator<PlayerScore> scoreIt = scores.getScores().descendingIterator(); scoreIt.hasNext() && itCount < topScoresCount;)
            {
                PlayerScore nextScore = scoreIt.next();
                name = new JLabel(nextScore.getName());
                time = new JLabel(getClockFormat(nextScore.getTime()));
                score = new JLabel(nextScore.getScore());
                if (nextScore.equals(playerScore))
                {
                    Color topScoreCol = new Color(127, 67, 196);
                    name.setForeground(topScoreCol);
                    time.setForeground(topScoreCol);
                    score.setForeground(topScoreCol);
                }
                Insets topPadding = new Insets(10,0,0,0);
                gblGroup.addToList(new GBLLeaf(0,5+itCount, 1, 1, topPadding, name));
                gblGroup.addToList(new GBLLeaf(1, 5+itCount, 1, 1, topPadding, time));
                gblGroup.addToList(new GBLLeaf(2, 5+itCount, 1, 1, topPadding, score));
                itCount += 1;
            }
        }

        // sets background black and opaque, as using overlaylayout to show ontop of chess board
        setBackground(new Color((float) 0.0,(float) 0.0, (float) 0.0, (float) 0.6));
        scores.writeScores();
        gblGroup.addGBL(gbc, gbl, this);
    }

    private String getClockFormat(int milliseconds)
    {
        int totSeconds = milliseconds / 1000;
        int hours = totSeconds / 3600;
        int minutes = totSeconds % 3600;
        int seconds = minutes / 60;
        return ((hours > 0)? hours + "hrs, ": "") + ((minutes > 0)? minutes + "mins, ": "")
                + seconds + "secs.";
    }
}
