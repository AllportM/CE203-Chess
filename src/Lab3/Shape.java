package Lab3;

import javax.swing.*;
import java.awt.*;

public abstract class Shape  extends JFrame {
    protected int posX, posY;

    abstract public double calcArea();

    abstract public void draw(Graphics g);
//    public Shape(int posX, int posY)
//    {
//        this.posX = posX;
//        this.posY = posY;
//    }
}
