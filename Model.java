import java.util.ArrayList;
import org.apache.commons.math4.legacy.fitting.PolynomialCurveFitter;
import org.apache.commons.math4.legacy.fitting.WeightedObservedPoints;


/*
 * Handles all functions with lists of positions
 * */
public class Model {

    public static int checkForChange(ArrayList<Node> nList){

        if(nList.size() < 2) //Too few nodes to use in prediction
            return 1;

        Node curPos = nList.get(nList.size()-1);
        Node prevPos = nList.get(nList.size()-2);

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

        int change = checkForChange(n);
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
    public static double[] predictionPolynomialRegression(ArrayList<Node> n, int degree, double simTime) {

        WeightedObservedPoints obsX = new WeightedObservedPoints();
        WeightedObservedPoints obsY = new WeightedObservedPoints();

        /*int status = checkForChange(n);
        if (status == 1)
            degree = 5;
        else if (status == -1)
            degree = 2;*/

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

    public static ArrayList<Node> getPredictionList(ArrayList<Node> nList, double t, int noOfDataPoints, int flag) {//0 for old prediction model, 1 for polynomial regression model

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Node> predictionList = new ArrayList<>();
        double[] predPos = new double[2];
        Node tempNode;
        for(Node n : nList) {
            temp.add(n);
            if(flag == 0)
                predPos = predictNextPosition(temp,t,noOfDataPoints);
            else if(flag == 1)
                predPos = predictionPolynomialRegression(temp,noOfDataPoints-1,t);
            tempNode = new Node(n.timeStep+1,n.id,predPos[0],predPos[1],n.v);
            predictionList.add(tempNode);
        }
        return predictionList;
    }

    public static double[] coordinateRMSE(ArrayList<Node> actualPos, ArrayList<Node> predictedPos, double t, int noDataPoints) { //flag: 0 for the old way, 1 for polynomial regression

        double RMSEx = 0;
        double RMSEy = 0;

        for(int i = 1; i < actualPos.size(); i++) {
            RMSEx += Math.pow(actualPos.get(i).x - predictedPos.get(i-1).x,2);
            RMSEy += Math.pow(actualPos.get(i).y - predictedPos.get(i-1).y,2);
        }

        RMSEx = Math.sqrt(RMSEx / predictedPos.size());
        RMSEy = Math.sqrt(RMSEy / predictedPos.size());

        return new double[]{RMSEx, RMSEy};
    }

    public static double positionalRMSE(ArrayList<Node> actualPos, ArrayList<Node> predictedPos) { //0 for old prediction model, 1 for polynomial prograssion model

        Node tempNode;
        double RMSE = 0;
        int timeStep = actualPos.get(0).timeStep;
        int id = actualPos.get(0).id;

        for(int i = 1; i < actualPos.size(); i++) {
            tempNode = new Node(++timeStep, id, predictedPos.get(i-1).x, predictedPos.get(i-1).y, actualPos.get(i).v);//Temporarirly get the v from actual position will predict it later
            RMSE += Math.pow(tempNode.distanceBetween(actualPos.get(i)),2);
        }

        return Math.sqrt(RMSE / actualPos.size());
    }
}
