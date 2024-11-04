import java.util.Comparator;
/*
 * Handles all Node functions
 * */
public class Node implements Comparable<Node>{

    int timeStep;
    int id;
    double x;
    double y;
    double v;

    public Node(int timeStep, int id, double x, double y, double v) {

        this.timeStep = timeStep;
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.timeStep, o.timeStep);
    }

    public double distanceBetween(Node n) {

        return Math.sqrt(Math.pow(this.x - n.x,2) + Math.pow(this.y-n.y,2));
    }

    public double lengthOf() {

        return Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
    }
    public double directionOfVector(Node n) {

        //Initial point in vector is "this" node, while the direction is determined by node "n".
        double vectorX = n.x - this.x;
        double vectorY = n.y - this.y;

        //Determine what quadrant the vector lies in
        return Math.atan(vectorY / vectorX) * 180 / Math.PI;
    }

    public double angleBetween(Node n1, Node n2) { //"this" is current node, n1 is two nodes back, and n2 is previous node.

        return Math.abs(n1.directionOfVector(n2) - n2.directionOfVector(this));
    }

    public String direction(Node n1, Node n2) { //"this" is predicted position, n1 is current position, and n2 is previous position

        if(this.angleBetween(n2,n1) < 4)
            return "Straight";
        return (n2.directionOfVector(n1) > n1.directionOfVector(this)) ? "Right" : "Left";
    }

    public double getVx(Node n) { //Input is the current position
        return n.v * Math.cos(this.directionOfVector(n));
    }

    public double getVy(Node n) { //Input is the current position
        return n.v * Math.sin(this.directionOfVector(n));
    }

    public String toString(){
        return "\n" + "Time step: " + timeStep +
                " ID: " + id + "\n" +
                "X: " + x + ", Y: " + y + "\n" +
                "Velocity: " + v;
    }

    public double getAcceleration(Node n, double t) { //"this" is current node, n is previous node
        return (this.v - n.v) / t;
    }
}
