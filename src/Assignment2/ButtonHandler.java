package Assignment2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Button  handler's main purpose is to handle action events from individual button clicks
 */
public class ButtonHandler implements ActionListener {
    ChessUI app; // main reference to the chess ui, in which access to a chess board instance is attained and all buttons

    // default constructor, initializes app member variable
    ButtonHandler(ChessUI ui)
    {
        app = ui;
    }

    // overrides the actionPerformed method
    @Override
    public void actionPerformed(ActionEvent e) {
        // start game button action from welcome screen
        if (e.getSource() == app.startGame)
        {
            ButtonGroup teamChoices = ((PlayerWelcome)((JButton)e.getSource()).getParent()).teamChoices;
            String teamCol = teamChoices.getSelection().getActionCommand();
            ButtonGroup opChoices = ((PlayerWelcome)((JButton)e.getSource()).getParent()).oppChoices;
            String opChoice = opChoices.getSelection().getActionCommand();
            String name = ((PlayerWelcome)((JButton)e.getSource()).getParent()).playerName.getText();
            app.initChess(teamCol, opChoice, name);
        }
        // new game menu action
        if(e.getSource() == app.getMenu().getNewGame())
        {
            app.initPlayerWelcome();
        }
        // revert move menu action
        if (e.getSource() == app.getMenu().getRevert() && app.chessBoard.getPreviousBoardStates().size() > 0)
        {
            app.chessBoard.revertMove();
            app.chessBoard.clearColouredTiles();
            app.repaint();
        }
        // display high scores menu action
        if (e.getSource() == app.getMenu().getHighScores())
        {
            app.initScores();
        }
        // close button from high score view action
        if (e.getSource() instanceof JButton && ((JButton) e.getSource()).getText() == "Close")
        {
            app.hideScores();
        }
    }
}
