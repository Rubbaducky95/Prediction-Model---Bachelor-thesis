import java.util.ArrayList;
import org.apache.commons.math4.legacy.fitting.PolynomialCurveFitter;
import org.apache.commons.math4.legacy.fitting.WeightedObservedPoints;
/*
 * Handles all functions with lists of positions.
 */
public class Model {

    public static int checkForChange(ArrayList<Node> nList){ //Return value will be what degree the polynomial regression model will use

        if(nList.size() < 2) //Too few nodes to use in prediction
            return 1;

        Node curPos = nList.get(nList.size()-1);
        Node prevPos = nList.get(nList.size()-2);

        if(nList.size() < 3) { //Can't check for angle change with only two nodes
            if (curPos.v - prevPos.v >= 0.5)
                return 1; //We are accelerating, keep going straight.
            else if (curPos.v - prevPos.v <= -0.5)
                return 2; //We are de-accelerating, we might turn.
            else
                return 1; //No significant change, we keep going straight.
        }

        Node prevPrevPos = nList.get(nList.size()-3);

        if(curPos.v - prevPos.v >= 0.5 || curPos.angleBetween(prevPrevPos, prevPos) <= 4) {
            return 1; //We are accelerating, keep going straight.
        }
        else if(curPos.v - prevPos.v <= -0.5 || curPos.angleBetween(prevPrevPos, prevPos) > 4) {
            if (curPos.angleBetween(prevPrevPos, prevPos) > 60) {
                return 3; //Big angle -> more DPs
            }
            return 2; //We are de-accelerating, we might turn.
        }
        else {
            return 1; //No significant change, we keep going straight.
        }
    }
    //Check if we have a certain change in velocity, distance and angle.
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
    //Old prediction model.
    public static double[] predictionPolynomialRegression(ArrayList<Node> n, int degree, double simTime, int useAngleCheck) {

        WeightedObservedPoints obsX = new WeightedObservedPoints();
        WeightedObservedPoints obsY = new WeightedObservedPoints();

        if(useAngleCheck == 1) { //Use prediction model with automatic data point adjustment

            degree = checkForChange(n);
        }

        if(n.size() < degree + 1) { //If we have more data points to look at than there are nodes in the list we go here.

            //Number of data points to look at is limited to the size of the list
            for (int i = 0; i < n.size(); i++) {

                double t = n.get(i).timeStep / simTime;
                double x = n.get(i).x;
                double y = n.get(i).y;
                obsX.add(t, x);
                obsY.add(t, y);
            }

            PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n.size() - 1);

            double[] coX = fitter.fit(obsX.toList());
            double[] coY = fitter.fit(obsY.toList());

            double newX = 0;
            double newY = 0;

            for (int i = 0; i < n.size(); i++) {

                newX += coX[i] * Math.pow(((double) n.get(n.size() - 1).timeStep + 1) / simTime, i);
                newY += coY[i] * Math.pow(((double) n.get(n.size() - 1).timeStep + 1) / simTime, i);
            }

            return new double[]{newX, newY};
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
    //Prediction using polynomial regression. Can either have an automatic datapoint changer activated or a set no. data points.
    public static ArrayList<Node> getPredictionList(ArrayList<Node> nList, double t, int noDataPoints, int flag) {//flag = 0 for no angle check and 1 for checking angle

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Node> predictionList = new ArrayList<>();

        for(Node n : nList) {

            temp.add(n);
            double[] predPos = predictionPolynomialRegression(temp,noDataPoints-1,t,flag);
            predictionList.add(new Node(n.timeStep+1,n.id,predPos[0],predPos[1],n.v));
        }
        return predictionList;
    }
    //Return a list containing predicted positions.
    public static ArrayList<Node> getMeanPredictionList(ArrayList<Node> nList, double t, int n) {

        //Return the mean coordinate for n-1 no. of coordinates
        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<Node> predictionList = new ArrayList<>();
        double[] predPosMean = {0.0, 0.0};

        for(Node node : nList) {

            temp.add(node);
            for(int i = 1; i < n; i++) {
                double[] predPos = predictionPolynomialRegression(temp, n - i, t, 0);   //Look at  data points
                predPosMean[0] += predPos[0];
                predPosMean[1] += predPos[1];
            }
            predPosMean[0] /= (n-1);
            predPosMean[1] /= (n-1);
            predictionList.add(new Node(node.timeStep + 1, node.id, predPosMean[0], predPosMean[1], node.v));
            predPosMean[0] = 0;
            predPosMean[1] = 0;
        }
        return predictionList;
    }
    //Return a list containing the mean predicted position of n to 2 datapoint predictions.
    public static ArrayList<APFP> getAPFPList(ArrayList<Node> nList, double t, int noDataPoints, int flag) {

        ArrayList<Node> temp = new ArrayList<>();
        ArrayList<APFP> APFPList = new ArrayList<>();
        Node prevPos = nList.get(0);
        Node prevPrevPos = nList.get(0);

        for(Node n : nList) {

            temp.add(n);
            double[] predCord = predictionPolynomialRegression(temp,noDataPoints-1,t,flag);
            Node predPos = new Node(n.timeStep+1,n.id,predCord[0],predCord[1],n.v);
            if(temp.size() >= 3) {

                double theta = n.angleBetween(prevPrevPos, prevPos) * Math.PI / 180; //In radians
                if(checkForChange(temp) > 1)
                    theta = theta * 2;

                /*double omega = n.directionOfVector(prevPos) * Math.PI / 180;
                double r = n.distanceBetween(predPos);
                double posX = n.x + r * (Math.cos(omega) * Math.cos(theta) - Math.sin(omega) * Math.sin(theta));
                double posY = n.y + r * (Math.sin(omega) * Math.cos(theta) + Math.cos(omega) * Math.sin(theta));
                double negX = n.x + r * (Math.cos(omega) * Math.cos(theta) - Math.sin(omega) * Math.sin(theta));
                double negY = n.y + r * (Math.sin(omega) * Math.cos(theta) - Math.cos(omega) * Math.sin(theta));*/


                double[] CP = {predPos.x-n.x, predPos.y-n.y}; //CP[0] = v_x, CP[1] = v_y
                double posX = n.x + CP[0] * Math.cos(theta) - CP[1] * Math.sin(theta);
                double posY = n.y + CP[0] * Math.sin(theta) + CP[1] * Math.cos(theta);
                double negX = n.x + CP[0] * Math.cos(-theta) - CP[1] * Math.sin(-theta);
                double negY = n.y + CP[0] * Math.sin(-theta) + CP[1] * Math.cos(-theta);


                APFPList.add(new APFP(n,predPos,posX,posY,negX,negY,theta * 180 / Math.PI));
            }
            else
                APFPList.add(new APFP(n,predPos,predPos.x,predPos.y,predPos.x,predPos.y,0));

            prevPrevPos = prevPos;
            prevPos = n;
        }
        return APFPList;
    }
    //Returns APFP with coordinates for edge points of are and angle. Does not use the mean-poly-reg prediction model as of now...
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
    //Return RMSE for both x and y.
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
    //Return the difference in distance RMSE.
    public static ArrayList<Double> getDifferenceInDistanceList(ArrayList<Node> actualPos, ArrayList<Node> predictedPos) {

        ArrayList<Double> diffList = new ArrayList<>();
        int i = 0;
        for(Node n : actualPos){
            diffList.add(n.distanceBetween(predictedPos.get(i)));
            i++;
        }
        return diffList;
    }
    //Return an array of differences in distance between actual and predicted position for all positions.
}
