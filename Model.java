import java.util.ArrayList;

public class Model {

    public static int checkForChange(ArrayList<Node> nList){

        if(nList.size() < 2) //Too few nodes to use in prediction
            return 0;

        Node curPos = nList.get(nList.size()-1);
        Node prevPos = nList.get(nList.size()-2);

        if(curPos.v - prevPos.v >= 0.5 || curPos.distanceBetween(prevPos) >= 4)
            return 1; //We are accelerating, keep going straight.

        else if(curPos.v - prevPos.v <= -0.5)
            return -1; //We are de-accelerating, we might turn.

        else
            return 1; //No significant change, we keep going straight.
    }
    public static double[] predictNextPosition(ArrayList<Node> n) { //Input must be a list of all nodes up until the current position.

        double[] newPos = new double[2];

        if(n.size() == 1){
            newPos[0] = n.get(0).x;
            newPos[1] = n.get(0).y;
            return newPos;
        }

        int change = checkForChange(n);
        double t = 0.1; //Change this value depending on what you simulate with.
        Node curPos = n.get(n.size() - 1);
        Node prevPos = n.get(n.size() - 2);

        if(change == 1) { //We are accelerating or have a constant velocity.
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t;
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t;
        }
        else if(change == -1) {
            //Do something with the angle also hmm...
        }

        return newPos;
    }
}
