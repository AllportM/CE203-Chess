package Lab3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandler implements ActionListener {
    DrawCalc theapp;
    JButton but;

    public ButtonHandler(DrawCalc app, JButton butt)
    {
        theapp = app;
        but = butt;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (but.getText())
        {
            case ("Set Length"):
                try
                {
                    this.theapp.square.setSideLength(
                            Integer.valueOf(theapp.newField.getText()));
                    theapp.repaint();
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(theapp.getRootPane(),
                            "Cannot cast '" + theapp.newField.getText()
                     + "' to an Integer");
                }
                break;
            case ("Calc Area"):
                theapp.newField.setText(String.valueOf(theapp.square.calcArea()));
                break;
        }
    }
}
