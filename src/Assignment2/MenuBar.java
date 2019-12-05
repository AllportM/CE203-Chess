package Assignment2;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
    private JMenu file;
    private JMenuItem newGame;
    private ChessUI ui;

    public MenuBar(ChessUI ui)
    {
        file = new JMenu("File");
        newGame = new JMenuItem("New Game");
        this.ui = ui;
        newGame.addActionListener(ui.buttonH);
        file.add(newGame);
        add(file);
    }

    public JMenuItem getNewGame()
    {
        return newGame;
    }
}
