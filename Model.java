import java.util.ArrayList;

public class Model {

    public int checkForChange(ArrayList<Node> nList){

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
    public double[] predictNextPosition(ArrayList<Node> n) {
        return null;
    }
}
