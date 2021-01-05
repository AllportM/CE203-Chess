package Assignment2;

import javax.swing.*;
import java.awt.*;

/*
 * MenuBar's main purpose is to create JMenu and JMenuItems to be attached to the main JFrame and store
 * items for access and identification in the button handler
 */
public class MenuBar extends JMenuBar {
    private JMenu file; // main file option in the menu, group of menu items
    private JMenuItem newGame; // new game menu item
    private JMenuItem revert; // revert item
    private JMenuItem highScores; // highscores
    private ChessUI ui; // reference to the ui

    /*
     * Default constructor, instantiates menuitems, menu, adds button listeners
     */
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

    // get methods for the main variables used in the button handler
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
