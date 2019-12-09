package Assignment2;

public class Node {
    Node parent;
    Board b;

    public Node (Board b)
    {
        this.b = b;
    }

    public Node (Node parent, Board b)
    {
        this.parent = parent;
        this.b = b;
    }

}
