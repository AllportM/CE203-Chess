package com.lab2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonHandler implements ActionListener
{
    FilledFrame theApp;
    JButton but;

    public ButtonHandler(FilledFrame theApp, JButton button)
    {
        but = button;
        this.theApp = theApp;
    }

    @Override
    public void actionPerformed (ActionEvent e)
    {
        switch (but.getText())
        {
            case "Small" :
                theApp.size = 50;
                break;
            case "Medium" :
                theApp.size = 100;
                break;
            case "Large" :
                theApp.size = 200;
                break;
        }
        theApp.repaint();
    }
}
