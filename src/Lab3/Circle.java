package Lab3;

import java.awt.*;

public class Circle extends Shape {
    protected int radius;

    public Circle(int posX, int posY, int radius)
    {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
    }


    @Override
    public double calcArea() {
        return Math.pow(radius, 2) * Math.PI;
    }

    @Override
    public void draw(Graphics g) {

    }
}
