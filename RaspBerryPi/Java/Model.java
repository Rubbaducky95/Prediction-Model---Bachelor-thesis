import java.util.ArrayList;
import org.apache.commons.math4.legacy.fitting.PolynomialCurveFitter;
import org.apache.commons.math4.legacy.fitting.WeightedObservedPoints;

/*
 * Handles all functions with lists of positions
 */
public class Model {

    public static int checkForChange(ArrayList<VRU> nList) { // Return value will be what degree the polynomial
                                                             // regression model will use

        if (nList.size() < 2) // Too few nodes to use in prediction
            return 1;

        VRU curPos = nList.get(nList.size() - 1);
        VRU prevPos = nList.get(nList.size() - 2);

        if (nList.size() < 3) { // Can't check for angle change with only two nodes
            if (curPos.v - prevPos.v >= 0.5)
                return 1; // We are accelerating, keep going straight.
            else if (curPos.v - prevPos.v <= -0.5)
                return 2; // We are de-accelerating, we might turn.
            else
                return 1; // No significant change, we keep going straight.
        }

        VRU prevPrevPos = nList.get(nList.size() - 3);

        if (curPos.v - prevPos.v >= 0.5 || curPos.angleBetween(prevPrevPos, prevPos) <= 4) {
            return 1; // We are accelerating, keep going straight.
        } else if (curPos.v - prevPos.v <= -0.5 || curPos.angleBetween(prevPrevPos, prevPos) > 4) {
            if (curPos.angleBetween(prevPrevPos, prevPos) > 60) {
                return 3; // Big angle -> more DPs
            }
            return 2; // We are de-accelerating, we might turn.
        } else {
            return 1; // No significant change, we keep going straight.
        }
    }

    public static double[] predictNextPosition(ArrayList<VRU> n, double t, int noDataPoints) {
        // Input must be a list of all nodes up until the current position,
        // what time we simulate with between each timestep,
        // and how many data points we look at.

        double[] newPos = new double[2];

        if (n.size() < noDataPoints) {
            newPos[0] = n.get(n.size() - 1).x;
            newPos[1] = n.get(n.size() - 1).y;
            return newPos;
        }

        int change = checkForChange(n);
        VRU curPos = n.get(n.size() - 1);
        VRU prevPos = n.get(n.size() - noDataPoints);

        if (change == 1) { // We are accelerating or have a constant velocity.
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t;
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t;
        } else if (change == -1) {
            // Do something with the angle also hmm...
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t;
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t;
        }

        return newPos;
    }

    public static double[] predictionPolynomialRegression(ArrayList<VRU> n, int degree, double simTime,
            int useAngleCheck) {

        WeightedObservedPoints obsX = new WeightedObservedPoints();
        WeightedObservedPoints obsY = new WeightedObservedPoints();

        if (useAngleCheck == 1) { // Use prediction model with automatic data point adjustment

            degree = checkForChange(n);
        }

        if (n.size() < degree + 1) { // If we have more data points to look at than there are nodes in the list we go
                                     // here.

            // Number of data points to look at is limited to the size of the list
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

            return new double[] { newX, newY };
        }

        else { // Look at the number of data points we chose, and predict next position.

            // Number of data points we look at is "degree" + 1.
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

            for (int i = 0; i < degree + 1; i++) {

                newX += coX[i] * Math.pow(((double) n.get(n.size() - 1).timeStep + 1) / simTime, i);
                newY += coY[i] * Math.pow(((double) n.get(n.size() - 1).timeStep + 1) / simTime, i);
            }

            return new double[] { newX, newY };
        }
    }

    public static ArrayList<VRU> getPredictionList(ArrayList<VRU> nList, double t, int noOfDataPoints, int flag) {// flag
                                                                                                                  // = 0
                                                                                                                  // for
                                                                                                                  // no
                                                                                                                  // angle
                                                                                                                  // check
                                                                                                                  // and
                                                                                                                  // 1
                                                                                                                  // for
                                                                                                                  // checking
                                                                                                                  // angle

        ArrayList<VRU> temp = new ArrayList<>();
        ArrayList<VRU> predictionList = new ArrayList<>();
        double[] predPos = new double[2];
        VRU tempNode;
        for (VRU n : nList) {
            temp.add(n);
            predPos = predictionPolynomialRegression(temp, noOfDataPoints - 1, t, flag);
            tempNode = new VRU(n.timeStep + 1, n.id, predPos[0], predPos[1], n.v);
            predictionList.add(tempNode);
        }
        return predictionList;
    }

    public static ArrayList<VRU> getMeanPredictionList(ArrayList<VRU> nList, double t, int n) {

        // Return the mean coordinate for n-1 no. of coordinates
        ArrayList<VRU> temp = new ArrayList<>();
        ArrayList<VRU> predictionList = new ArrayList<>();
        double[] predPosMean = { 0.0, 0.0 };

        for (VRU node : nList) {

            temp.add(node);
            for (int i = 1; i < n; i++) {
                double[] predPos = predictionPolynomialRegression(temp, n - i, t, 0); // Look at data points
                predPosMean[0] += predPos[0];
                predPosMean[1] += predPos[1];
            }
            predPosMean[0] /= (n - 1);
            predPosMean[1] /= (n - 1);
            predictionList.add(new VRU(node.timeStep + 1, node.id, predPosMean[0], predPosMean[1], node.v));
            predPosMean[0] = 0;
            predPosMean[1] = 0;
        }
        return predictionList;
    }
    // Return a list containing the mean predicted position of n to 2 datapoint
    // predictions.

    public static ArrayList<APFP> getAPFPList(ArrayList<VRU> nList, double t, int noDataPoints, int flag) { //flag = 0 for normal, = 1 for angle check, = 2 for mean

        ArrayList<VRU> temp = new ArrayList<>();
        ArrayList<APFP> APFPList = new ArrayList<>();
        VRU prevPos = nList.get(0);
        VRU prevPrevPos = nList.get(0);
        VRU predPos;
        double[] predPosMean = {0.0, 0.0};

        for (VRU n : nList) {

            temp.add(n);
            if(flag == 2) {
                for(int i = 1; i < noDataPoints; i++) {
                    double[] predCord = predictionPolynomialRegression(temp, noDataPoints - i, t, 0);   //Look at  data points
                    predPosMean[0] += predCord[0];
                    predPosMean[1] += predCord[1];
                }
                predPosMean[0] /= (noDataPoints-1);
                predPosMean[1] /= (noDataPoints-1);
                predPos = new VRU(n.timeStep + 1, n.id, predPosMean[0], predPosMean[1], n.v);
                predPosMean[0] = 0;
                predPosMean[1] = 0;
            }

            else {
                double[] predCord = predictionPolynomialRegression(temp, noDataPoints - 1, t, flag);
                predPos = new VRU(n.timeStep + 1, n.id, predCord[0], predCord[1], n.v);
            }


            if (temp.size() >= 3) {

                double theta = n.angleBetween(prevPrevPos, prevPos) * Math.PI / 180; // In radians
                if (checkForChange(temp) == 3)
                    theta = theta   * 2;//If we want to factor theta to encompass a larger area when a big turn is coming

                double omega = n.directionOfVector(prevPos) * Math.PI / 180;
                double r = n.distanceBetween(predPos);
                double posX = n.x + r * (Math.cos(omega) * Math.cos(theta) - Math.sin(omega) * Math.sin(theta));
                double posY = n.y + r * (Math.sin(omega) * Math.cos(theta) + Math.cos(omega) * Math.sin(theta));
                double negX = n.x + r * (Math.cos(omega) * Math.cos(theta) + Math.sin(omega) * Math.sin(theta));
                double negY = n.y + r * (Math.sin(omega) * Math.cos(theta) - Math.cos(omega) * Math.sin(theta));

                String direction = predPos.direction(n, prevPos);

                APFPList.add(new APFP(n, predPos, posX, posY, negX, negY, theta * 180 / Math.PI, direction));
            } else
                APFPList.add(new APFP(n, predPos, predPos.x, predPos.y, predPos.x, predPos.y, 0, "Straight"));

            prevPrevPos = prevPos;
            prevPos = n;
        }
        return APFPList;
    }
    // Returns APFP with coordinates for edge points of are and angle. Does not use
    // the mean-poly-reg prediction model as of now...

    public static double[] coordinateRMSE(ArrayList<VRU> actualPos, ArrayList<VRU> predictedPos, double t,
            int noDataPoints) { // flag: 0 for the old way, 1 for polynomial regression

        double RMSEx = 0;
        double RMSEy = 0;

        for (int i = 1; i < actualPos.size(); i++) {
            RMSEx += Math.pow(actualPos.get(i).x - predictedPos.get(i - 1).x, 2);
            RMSEy += Math.pow(actualPos.get(i).y - predictedPos.get(i - 1).y, 2);
        }

        RMSEx = Math.sqrt(RMSEx / predictedPos.size());
        RMSEy = Math.sqrt(RMSEy / predictedPos.size());

        return new double[] { RMSEx, RMSEy };
    }

    public static double positionalRMSE(ArrayList<VRU> actualPos, ArrayList<VRU> predictedPos) { // 0 for old prediction
                                                                                                 // model, 1 for
                                                                                                 // polynomial
                                                                                                 // prograssion model

        VRU tempNode;
        double RMSE = 0;
        int timeStep = actualPos.get(0).timeStep;
        int id = actualPos.get(0).id;

        for (int i = 1; i < actualPos.size(); i++) {
            tempNode = new VRU(++timeStep, id, predictedPos.get(i - 1).x, predictedPos.get(i - 1).y,
                    actualPos.get(i).v);// Temporarirly get the v from actual position will predict it later
            RMSE += Math.pow(tempNode.distanceBetween(actualPos.get(i)), 2);
        }

        return Math.sqrt(RMSE / actualPos.size());
    }

    public static ArrayList<Double> getDifferenceInDistanceList(ArrayList<VRU> actualPos, ArrayList<VRU> predictedPos) {

        ArrayList<Double> diffList = new ArrayList<>();
        int i = 0;
        for (VRU n : actualPos) {
            diffList.add(n.distanceBetween(predictedPos.get(i)));
            i++;
        }
        return diffList;
    }
}
