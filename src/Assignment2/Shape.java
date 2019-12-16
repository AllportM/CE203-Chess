package Assignment2;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * The shape interface is a composite design pattern, with each individual shape  being created and added to a
 * Complex shape. The renderShape is therefor different for each sub shape and implemented therein
 */
interface Shape
{
    void renderShape(Graphics g); // main draw method for each shape
}

/*
 * Rectangle's purpose is to create a filled rectangle, with a 1mm grey border
 */
class Rectangle implements Shape
{
    private int x1, x2, y1, y2; // position values for a rectangle
    private Color COLOUR; // colour

    // standard constructor
    Rectangle(int x1, int x2, int y1, int y2, Color colour)
    {
        COLOUR = colour;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    // overrides the renderShape to draw this specific shape
    @Override
    public void renderShape(Graphics g)
    {
        g.setColor(Color.gray);
        g.fillRect(x1, y1, x2-x1, y2-y1);
        g.setColor(COLOUR);
        g.fillRect(x1 + 1, y1 + 1, x2-x1-2, y2-y1-2);
    }
}

/*
 * Base's purpose is to draw a filled rectangle at the same position for each piece
 */
class Base implements Shape
{
    private Color COLOUR;

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
    private int x, y, width, height;
    private Color COLOUR;

    Oval(int x, int y, int width, int height, Color colour)
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
    private int[] x, y;
    private int[] x2 = new int[3];
    private int[] y2 = new int[3];
    private Color COLOUR;

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
    private int x, y, r;
    Color COLOUR;

    Circle(int x, int y, int r, Color colour)
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
    private int[] x, y;
    private Color COLOUR;

    Poly(int[] x, int[] y, Color col)
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

/*
 * Complex shape is designed to add a group of shapes to its list, and then loop through them during the renderShape
 * method calling each individual shapes renderShape methods
 */
class ComplexShape implements Shape
{
    private List<Shape> shapeList;

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