import java.util.Comparator;
/*
* Handles all Node functions
* */
public class VRU implements Comparable<VRU>{

    int timeStep;
    int id;
    double x;
    double y;
    double v;

    public VRU(int timeStep, int id, double x, double y, double v) {

        this.timeStep = timeStep;
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
    }

    @Override
    public int compareTo(VRU o) {
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

    public double distanceBetween(VRU n) {

        return Math.sqrt(Math.pow(this.x - n.x,2) + Math.pow(this.y-n.y,2));
    }

    public double lengthOf() {

        return Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
    }

    public double directionOfVector(VRU n) {

        //Initial point in vector is "this" node, while the direction is determined by node "n".
        double vectorX = n.x - this.x;
        double vectorY = n.y - this.y;

        return Math.atan(vectorY / vectorX) * 180 / Math.PI;
    }

    public double angleBetween(VRU n1, VRU n2) { //"this" is current node, n1 is two nodes back, and n2 is previous node.

        return Math.abs(n1.directionOfVector(n2) - n2.directionOfVector(this));
    }

    public String direction(VRU n1, VRU n2) { //"this" is predicted position, n1 is current position, and n2 is previous position

        if(this.angleBetween(n2,n1) < 4)
            return "Straight";
        return (n2.directionOfVector(n1) > n1.directionOfVector(this)) ? "Left" : "Right"; //Should be opposite but it works with the draw_positions.py
    }

    public double getVx(VRU n) { //Input is the current position
        return n.v * Math.cos(this.directionOfVector(n));
    }

    public double getVy(VRU n) { //Input is the current position
        return n.v * Math.sin(this.directionOfVector(n));
    }

    public String toString(){
        return "\n" + "Time step: " + getTimeStep() +
                " ID: " + getId() + "\n" +
                "X: " + getX() + ", Y: " + getY() + "\n" +
                "Velocity: " + getV();
    }
}
