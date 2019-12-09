package Assignment2;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
    private JMenu file;
    private JMenuItem newGame;
    private JMenuItem revert;
    private ChessUI ui;

    public MenuBar(ChessUI ui)
    {
        file = new JMenu("File");
        newGame = new JMenuItem("New Game");
        revert = new JMenuItem("Revert Move");
        this.ui = ui;
        newGame.addActionListener(ui.buttonH);
        revert.addActionListener(ui.buttonH);
        file.add(newGame);
        file.add(revert);
        add(file);
    }

    public JMenuItem getNewGame()
    {
        return newGame;
    }

    public JMenuItem getRevert()
    {
        return revert;
    }
}
