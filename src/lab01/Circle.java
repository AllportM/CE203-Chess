package lab01;

public class Circle extends Shape {
    protected int centerX;
    protected int centerY;
    protected int radius;

    public Circle(int centerX, int centerY, int radius) {
        this.name = "Circle";
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    public void draw() {
        System.out.println("This is a circle with a center(" + centerX + ", " + centerY + ")and radius " + radius);
    }
}
