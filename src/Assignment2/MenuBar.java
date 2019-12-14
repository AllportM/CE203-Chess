package Assignment2;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
    private JMenu file;
    private JMenuItem newGame;
    private JMenuItem revert;
    private JMenuItem highScores;
    private ChessUI ui;

    public MenuBar(ChessUI ui)
    {
        file = new JMenu("File");
        newGame = new JMenuItem("New Game");
        revert = new JMenuItem("Revert Move");
        highScores = new JMenuItem("High Scores");
        this.ui = ui;
        newGame.addActionListener(ui.buttonH);
        revert.addActionListener(ui.buttonH);
        highScores.addActionListener(ui.buttonH);
        file.add(newGame);
        file.add(revert);
        file.add(highScores);
        add(file);
    }

    JMenuItem getNewGame()
    {
        return newGame;
    }

    JMenuItem getRevert()
    {
        return revert;
    }

    JMenuItem getHighScores() {
        return highScores;
    }
}
