package Lab3;

import java.awt.*;

public class Square extends Shape {
    private int sideLength;

    public Square(int posX, int posY, int sideLength)
    {
        this.posX = posX;
        this.posY = posY;
        this.sideLength = sideLength;
    }

    public void setSideLength(int sideLength)
    {
        this.sideLength = sideLength;
    }

    public double calcArea()
    {
        return Math.pow(sideLength, 2);
    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillRect(posX, posY, sideLength, sideLength);
    }
}
