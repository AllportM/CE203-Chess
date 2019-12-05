package lab01;

public class Rectangle extends Shape {
    protected int startX;
    protected int startY;
    protected int width;
    protected int height;

    public Rectangle (int startX, int startY, int width, int height){
        this.name = "Rectangle";
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public void draw() {
        System.out.println("This is a rectangle starting at (" + startX + ", " + startY + ") with a width of "
        + width + "mm and height of " + height + "mm.");
    }
}
