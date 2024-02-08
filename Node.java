import java.util.Comparator;
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

    public String toString(){
        return "\n" + "Time step: " + getTimeStep() +
                " ID: " + getId() + "\n" +
                "X: " + getX() + ", Y: " + getY() + "\n" +
                "Velocity: " + getV();
    }
}
