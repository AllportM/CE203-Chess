package Assignment2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandler implements ActionListener {
    ChessUI app;

    ButtonHandler(ChessUI ui)
    {
        app = ui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == app.startGame)
        {
            ButtonGroup teamChoices = ((PlayerWelcome)((JButton)e.getSource()).getParent()).teamChoices;
            String teamCol = teamChoices.getSelection().getActionCommand();
            ButtonGroup opChoices = ((PlayerWelcome)((JButton)e.getSource()).getParent()).oppChoices;
            String opChoice = opChoices.getSelection().getActionCommand();
            String name = ((PlayerWelcome)((JButton)e.getSource()).getParent()).playerName.getText();
            app.initChess(teamCol, opChoice, name);
        }
    }
}
