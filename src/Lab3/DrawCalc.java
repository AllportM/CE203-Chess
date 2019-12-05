package Lab3;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DrawCalc extends JFrame {
    Square square;
    TextField newField;

    public DrawCalc()
    {
        setSize(new Dimension(600, 600));
        square = new Square(10, 10, 50);
        newField = new TextField("",10);
        JPanel centreP = new JPanel() {
            public void paintComponent(Graphics g)
            {
                square.draw(g);
            }
        };
        JButton setLength = new JButton("Set Length");
        JButton calcA = new JButton("Calc Area");
        setLength.addActionListener(new ButtonHandler(this, setLength));
        calcA.addActionListener(new ButtonHandler(this, calcA));
        // sets button panel
        JPanel botP = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        botP.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel sqLabel = new JLabel("Square");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(sqLabel, gbc);
        botP.add(sqLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(newField, gbc);
        botP.add(newField);
        gbc.gridx = 1;
        gbl.setConstraints(setLength, gbc);
        botP.add(setLength);
        gbc.gridx = 2;
        gbl.setConstraints(calcA, gbc);
        botP.add(calcA);


//        botP.add(setLength);
//        botP.add(calcA);
//        botP.add(newField);
        add(centreP, BorderLayout.CENTER);
        add(botP, BorderLayout.SOUTH);



    }

    public static void main(String[] args) {
        new DrawCalc().setVisible(true);
    }
}
