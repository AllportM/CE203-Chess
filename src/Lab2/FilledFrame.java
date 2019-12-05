package Lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FilledFrame extends JFrame
{
    int size = 400;

    public FilledFrame()
    {
        JButton butSmall = new JButton("Small");
        JButton butMedium = new JButton("Medium");
        JButton butLarge = new JButton("Large");
        JButton butMessage = new JButton("Say Hi!");

        SquarePanel panel = new SquarePanel(this);
        JPanel butPanel = new JPanel();

        butPanel.add(butSmall);
        butPanel.add(butMedium);
        butPanel.add(butLarge);
        butPanel.add(butMessage);
        add(butPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setSize(size+100, size+100);

        butMessage.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                JOptionPane.showMessageDialog(FilledFrame.super.rootPane, "Hi i i i ");
            }
        });
        butSmall.addActionListener(new ButtonHandler(this, butSmall));
        butMedium.addActionListener(new ButtonHandler(this, butMedium));
        butLarge.addActionListener(new ButtonHandler(this, butLarge));

    }
}

