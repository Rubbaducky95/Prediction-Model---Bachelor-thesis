import java.util.ArrayList;
import org.apache.commons.math4.legacy.fitting.PolynomialCurveFitter;
import org.apache.commons.math4.legacy.fitting.WeightedObservedPoints;


/*
 * Handles all functions with lists of positions
 * */
public class Model {

    public static int checkForChange(ArrayList<Node> nList, int noDataPoints){

        if(nList.size() < noDataPoints) //Too few nodes to use in prediction
            return 0;

        Node curPos = nList.get(nList.size()-1);
        Node prevPos = nList.get(nList.size()-noDataPoints);

        if(curPos.v - prevPos.v >= 0.5 || curPos.distanceBetween(prevPos) >= 4)
            return 1; //We are accelerating, keep going straight.

        else if(curPos.v - prevPos.v <= -0.5)
            return -1; //We are de-accelerating, we might turn.

        else
            return 1; //No significant change, we keep going straight.
    }
    public static double[] predictNextPosition(ArrayList<Node> n, double t, int noDataPoints) {
        //Input must be a list of all nodes up until the current position,
        // what time we simulate with between each timestep,
        //and how many data points we look at.

        double[] newPos = new double[2];

        if(n.size() < noDataPoints){
            newPos[0] = n.get(n.size()-1).x;
            newPos[1] = n.get(n.size()-1).y;
            return newPos;
        }

        int change = checkForChange(n, noDataPoints);
        Node curPos = n.get(n.size()-1);
        Node prevPos = n.get(n.size()-noDataPoints);

        if(change == 1) { //We are accelerating or have a constant velocity.
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t;
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t;
        }
        else if(change == -1) {
            //Do something with the angle also hmm...
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t;
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t;
        }

        return newPos;
    }

    public static ArrayList<Node> getPredictionList(ArrayList<Node> nList) {

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Node> predictionList = new ArrayList<>();
        double[] predPos;
        Node tempNode;
        for(Node n : nList) {
            temp.add(n);
            predPos = Model.predictNextPosition(temp,0.5,2);
            tempNode = new Node(n.timeStep+1,n.id,predPos[0],predPos[1],n.v);
            predictionList.add(tempNode);
        }
        return predictionList;
    }

    public static double[] coordinateRMSE(ArrayList<Node> actualPos, double t, int noDataPoints, int flag) { //flag: 0 for the old way, 1 for polynomial regression

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Double> difference = new ArrayList<>();
        ArrayList<Double> newX = new ArrayList<>();
        ArrayList<Double> newY = new ArrayList<>();
        double[] predictedPos = new double[2];

        for(Node n : actualPos) {

            temp.add(n);

            if(flag == 0)
                predictedPos = predictNextPosition(temp, t, noDataPoints);
            else if(flag == 1)
                predictedPos = predictionPolynomialRegression(temp,noDataPoints-1,t);

            newX.add(predictedPos[0]);
            newY.add(predictedPos[1]);
        }

        double RMSEx = 0;
        double RMSEy = 0;
        int i = 0;

        for(Node n : temp) {
            RMSEx += Math.pow(n.x - newX.get(i),2);
            RMSEy += Math.pow(n.y - newY.get(i),2);
            i++;
        }

        RMSEx = Math.sqrt(RMSEx / temp.size());
        RMSEy = Math.sqrt(RMSEy / temp.size());

        return new double[]{RMSEx, RMSEy};
    }

    public static double positionalRMSE(ArrayList<Node> actualPos, double t, int noDataPoints) {

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Double> difference = new ArrayList<>();
        Node tempNode;
        double[] predictedPos;

        for(Node n : actualPos) {
            temp.add(n);
            predictedPos = predictNextPosition(temp, t, noDataPoints);
            tempNode = new Node(n.timeStep+1, n.id, predictedPos[0], predictedPos[1], n.v);
            difference.add(tempNode.distanceBetween(n));
        }

        double RMSE = 0;

        for(double dif : difference) {
            RMSE += Math.pow(dif,2);
        }

        return RMSE = Math.sqrt(RMSE / difference.size());
    }

    public static double[] predictionPolynomialRegression(ArrayList<Node> n, int degree, double simTime) {

        WeightedObservedPoints obsX = new WeightedObservedPoints();
        WeightedObservedPoints obsY = new WeightedObservedPoints();

        if(n.size() < degree + 1) { //If we have more data points to look at than there are nodes in the list we go here.

            //Number of data points to look at is limited to the size of the list
            for(int i = 0; i < n.size(); i++) {

                double t = n.get(i).timeStep / simTime;
                double x = n.get(i).x;
                double y = n.get(i).y;
                obsX.add(t, x);
                obsY.add(t, y);
            }

            PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n.size()-1);

            double[] coX = fitter.fit(obsX.toList());
            double[] coY = fitter.fit(obsY.toList());

            double newX = 0;
            double newY = 0;

            for(int i = 0; i < n.size(); i++) {

                newX += coX[i] * Math.pow(((double) n.get(n.size() - 1).timeStep+1) / simTime, i);
                newY += coY[i] * Math.pow(((double) n.get(n.size() - 1).timeStep+1) / simTime, i);
            }

            return new double[] {newX, newY};
        }

        else { //Look at the number of data points we chose, and predict next position.

            //Number of data points we look at is "degree" + 1.
            for (int i = 0; i < degree + 1; i++) {

                double t = n.get(n.size() - degree - 1 + i).timeStep / simTime;
                double x = n.get(n.size() - degree - 1 + i).x;
                double y = n.get(n.size() - degree - 1 + i).y;
                obsX.add(t, x);
                obsY.add(t, y);
            }

            PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);

            double[] coX = fitter.fit(obsX.toList());
            double[] coY = fitter.fit(obsY.toList());

            double newX = 0;
            double newY = 0;

            for(int i = 0; i < degree + 1; i++) {

                newX += coX[i] * Math.pow(((double) n.get(n.size() - 1).timeStep+1) / simTime, i);
                newY += coY[i] * Math.pow(((double) n.get(n.size() - 1).timeStep+1) / simTime, i);
            }

            return new double[] {newX, newY};
        }
    }
}
