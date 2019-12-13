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
        GridBagLayout gblInner = new GridBagLayout();
        GridBagConstraints gbcInner = new GridBagConstraints();
        ComplexGBL gblGroupInner = new ComplexGBL();
        JPanel inner = new JPanel();
        inner.setPreferredSize(new Dimension(400, 500));
        inner.setLayout(gblInner);

        GridBagLayout gblPrimary = new GridBagLayout();
        GridBagConstraints gbcPrimary = new GridBagConstraints();
        ComplexGBL gblGroupPrimary = new ComplexGBL();
        gblGroupPrimary.addToList(new GBLLeaf(0,1,inner));
        setLayout(gblPrimary);


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
        add(tile);
        gblGroupInner.addToList(new GBLLeaf(0, 0, 2, 2, new Insets(0, 50, 50, 0), tile));

        // adds gameover label
        String gOver = (((HumanPlayer) board.getP1()).getWinStatus() == WINNER)? "Congratulations! you Won!!!":
                ((HumanPlayer) board.getP1()).getWinStatus() == STALEMATE? "Congratulations, you reached a Stalemate":
                        "Sorry, you were beaten, better luck next time";
        JLabel gameover = new JLabel(gOver);
        gblGroupInner.addToList(new GBLLeaf(0, 2, 2, 1, new Insets(0, 30, 0, 0),gameover));

        JLabel pScore = new JLabel("Your score");
        gblGroupInner.addToList(new GBLLeaf(0, 3, 2, 1, new Insets(50, 50, 10, 0), pScore));

        // adds current player score to layout
        PlayerScore playerScore = new PlayerScore(board.getP1());
        JLabel name, time, score;
        name = new JLabel(playerScore.getName());
        time = new JLabel(getClockFormat(playerScore.getTime()));
        score = new JLabel(playerScore.getScore());
        gblGroupInner.addToList(new GBLLeaf(0, 4, name));
        gblGroupInner.addToList(new GBLLeaf(1, 4, time));
        gblGroupInner.addToList(new GBLLeaf(2, 4, score));


        scores.addScore(playerScore);

        // adds top 5 scores, if 5 scores exist
        JLabel topScores = new JLabel("Top 5 Scores");
        gblGroupInner.addToList(new GBLLeaf(0,5,2,1, new Insets(50,70,0,10), topScores));
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
                Insets topPadding = new Insets(10,10,0,10);
                if (itCount == topScoresCount-1) topPadding = new Insets(10,10,10,10);
                gblGroupInner.addToList(new GBLLeaf(0,6+itCount, 1, 1, topPadding, name));
                gblGroupInner.addToList(new GBLLeaf(1, 6+itCount, 1, 1, topPadding, time));
                gblGroupInner.addToList(new GBLLeaf(2, 6+itCount, 1, 1, topPadding, score));
                itCount += 1;
            }
        }

        JButton exit = new JButton("Close");
        exit.addActionListener(board.getUi().buttonH);
        gblGroupInner.addToList(new GBLLeaf(0, 7, 3, 1, exit));

        // sets background black and opaque, as using overlaylayout to show ontop of chess board
        setBackground(new Color((float) 0.0,(float) 0.0, (float) 0.0, (float) 0.6));
        scores.writeScores();
        gblGroupInner.addGBL(gbcInner, gblInner, inner);
        gblGroupPrimary.addGBL(gbcPrimary, gblPrimary, this);
    }

    private String getClockFormat(int milliseconds)
    {
        int totSeconds = milliseconds / 1000;
        int hours = totSeconds / 3600;
        int minutes = (totSeconds % 3600) / 60;
        int seconds = minutes / 60;
        return ((hours > 0)? hours + " hrs, ": "0 hrs, ") + ((minutes > 0)? minutes + " mins, ": "0 mins, ")
                + seconds + " secs.";
    }
}
