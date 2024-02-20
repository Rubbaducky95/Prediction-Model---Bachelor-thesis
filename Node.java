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

    public int getId(){
        return id;
    }

    public int getTimeStep(){
        return timeStep;
    }

    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }

    public double getV() {
        return v;
    }

    public double distanceBetween(Node n) {

        return Math.sqrt(Math.pow(this.x - n.x,2) + Math.pow(this.y-n.y,2));
    }

    public double angleBetween(Node n) {

        return Math.atan(Math.abs(this.y - n.y) / Math.abs(this.x - n.x));
    }

    public double getVx(Node n) { //Input is the current position
        return n.v * Math.cos(this.angleBetween(n));
    }

    public double getVy(Node n) { //Input is the current position
        return n.v * Math.sin(this.angleBetween(n));
    }

    public String toString(){
        return "\n" + "Time step: " + getTimeStep() +
                " ID: " + getId() + "\n" +
                "X: " + getX() + ", Y: " + getY() + "\n" +
                "Velocity: " + getV();
    }
}
