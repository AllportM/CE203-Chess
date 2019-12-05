package Assignment2;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

interface Shape
{
    void renderShape(Graphics g);
}

class Rectangle implements Shape
{
    int x1, x2, y1, y2;
    Color COLOUR;

    Rectangle(int x1, int x2, int y1, int y2, Color colour)
    {
        COLOUR = colour;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void changeCol(Color colour)
    {
        COLOUR = colour;
    }

    @Override
    public void renderShape(Graphics g)
    {
        g.setColor(Color.gray);
        g.fillRect(x1, y1, x2-x1, y2-y1);
        g.setColor(COLOUR);
        g.fillRect(x1 + 1, y1 + 1, x2-x1-2, y2-y1-2);
    }
}

class Base implements Shape
{
    Color COLOUR;

    Base(Color colour)
    {
        COLOUR = colour;
    }

    @Override
    public void renderShape(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(20, 75, 60, 15);
        g.setColor(COLOUR);
        g.fillRect(21, 76, 58, 13);
    }
}

class Oval implements Shape
{
    int x, y, width, height, rotate;
    Color COLOUR;

    public Oval(int x, int y, int width, int height, Color colour)
    {
        COLOUR = colour;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderShape(Graphics g)
    {
        g.setColor(Color.gray);
        g.setColor(Color.gray);
        g.fillOval(x, y, width, height);
        g.setColor(COLOUR);
        g.fillOval(x+1, y+1, width-2, height-2);
    }
}

class Triangle implements Shape
{
    int[] x, y;
    int[] x2 = new int[3];
    int[] y2 = new int[3];
    Color COLOUR;

    public Triangle(int[] x, int y[], Color colour)
    {
        COLOUR = colour;
        this.x = x;
        this.y = y;
        // sets inner triangle coordinates, assuming x,y[0] is top point, x,y[1] is left point, and x,y[2] is right
        x2[0] = x[0];
        x2[1] = x[1] + 1;
        x2[2] = x[2] - 1;
        y2[0] = y[0] + 1;
        y2[1] = y[1] -1;
        y2[2] = y[2] -1;
    }

    @Override
    public void renderShape(Graphics g) {
        g.setColor(Color.gray);
        g.fillPolygon(x, y, 3);
        g.setColor(COLOUR);
        g.fillPolygon(x2, y2, 3);
    }
}

class Circle implements Shape
{
    int x, y, r;
    Color COLOUR;

    public Circle(int x, int y, int r, Color colour)
    {
        COLOUR = colour;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void renderShape(Graphics g) {
        g.setColor(Color.gray);
        g.fillOval(x-r, y-r, r*2, r*2);
        g.setColor(COLOUR);
        g.fillOval(x-r+1, y-r +1, r*2 - 2, r*2 - 2);
    }
}

class Poly implements Shape
{
    int[] x, y;
    Color COLOUR;

    public Poly(int[] x, int[] y, Color col)
    {
        COLOUR = col;
        this.x = x;
        this.y = y;
    }

    @Override
    public void renderShape(Graphics g) {
        g.setColor(COLOUR);
        g.fillPolygon(x, y, 4);
        g.setColor(Color.gray);
        g.drawPolygon(x, y, 4);
    }
}

class ComplexShape implements Shape
{
    List<Shape> shapeList;

    ComplexShape()
    {
        shapeList = new ArrayList<>();
    }

    void addToShape(Shape toAdd)
    {
        shapeList.add(toAdd);
    }

    @Override
    public void renderShape(Graphics g)
    {
        for (Shape x: shapeList)
        {
            x.renderShape(g);
        }
    }
}