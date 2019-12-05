package Lab2;

import javax.swing.*;
import java.awt.*;

class SquarePanel extends JPanel
{
    FilledFrame theApp;

    SquarePanel(FilledFrame app)
    {
        theApp = app;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.fillRect(20, 20, theApp.size, theApp.size);
    }
}
