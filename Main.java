import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argimport javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\Simulations\\Test\\output\\Zig_zag_2024-02-20_16-16-09.758";
        ReadOutput whatToRead = new ReadOutput(filePath);

        ArrayList<Node> VRU = whatToRead.getDataFor(1);

        ArrayList<Node> oldpredictionVRU = Model.getPredictionList(VRU,0.5,2,0);
        ArrayList<Node> newpredictionVRU = Model.getPredictionList(VRU,0.5,5,1);

        for (int i = 1; i < VRU.size(); i++)
            System.out.println(VRU.get(i) + " " + newpredictionVRU.get(i-1));

        System.out.println(Model.positionalRMSE(VRU,oldpredictionVRU));
        System.out.println(Model.positionalRMSE(VRU,newpredictionVRU));

        ArrayList<ArrayList<Node>> listOfNodeLists = new ArrayList<>();
        listOfNodeLists.add(VRU);
        listOfNodeLists.add(newpredictionVRU);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(listOfNodeLists, 20.0);
            frame.setVisible(true);
        });


        /*System.out.println(Model.positionalRMSE(VRU, 0.5, 4));
        double[] RMSEcord = Model.coordinateRMSE(VRU, 0.5, 4);
        System.out.println("x: " + RMSEcord[0] + ", " + "y: " + RMSEcord[1]);
        ArrayList<Node> test = new ArrayList<>();

        for(Node n : VRU) {
            test.add(n);
            System.out.println(Model.checkForChange(test, 2));
        }

        /*
        ArrayList<Node> VRU1 = whatToRead.getDataFor(1);
        ArrayList<Node> test = new ArrayList<>();
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for(Node n : VRU1) {
            xValues.add(n.getX());
            yValues.add(n.getY());
        }

        for(Node n : VRU1) {
            test.add(n);
            System.out.println(Model.checkForChange(test));
        }

        int [] timeStep = whatToRead.timeStepsFor(1);
        int time = timeStep[0];
        ArrayList<Double> newXValues = new ArrayList<>();
        ArrayList<Double> newYValues = new ArrayList<>();
        double[] newpos;
        for(Node n : VRU1) {
            test.add(n);
            newpos = Model.predictNextPosition(test);
            time++;
            System.out.println("Time step : " + time );
            System.out.print("Predicted xy: ");
            for(int i = 0; i < 2; i++) {
                System.out.print(newpos[i] + " ");
            }
            System.out.print("\nActual position: " + xValues.get(time-1) + " " + yValues.get(time-1));
            System.out.println("\n");
            newXValues.add(newpos[0]);
            newYValues.add(newpos[1]);
        }

        double RMSEx = 0.0;
        double RMSEy = 0.0;
        for(int i = 0; i < timeStep[1]-timeStep[0]+1; i++){
            RMSEx += Math.pow(xValues.get(i+1) - newXValues.get(i),2);
            RMSEy += Math.pow(yValues.get(i+1) - newYValues.get(i),2);
        }
        RMSEx = Math.sqrt(RMSEx / (timeStep[1] - timeStep[0]+1));
        RMSEy = Math.sqrt(RMSEy / (timeStep[1] - timeStep[0]+1));


        System.out.println(timeStep[1]-timeStep[0]);
        System.out.println("Old x values: " + xValues.size() + ", New x values: " + newXValues.size());
        System.out.println(RMSEx + ", " + RMSEy);
         */

    }
}s) throws FileNotFoundException {

        String filePath = "C:\\Users\\ruben\\OneDrive\\Dokument\\Högskolan\\DCTVT24\\EXAMENS ARBETE\\Simulation\\Simulations\\Test\\output\\Curve_2024-02-20_15-48-11.782";
        ReadOutput whatToRead = new ReadOutput(filePath);

        ArrayList<Node> VRU = whatToRead.getDataFor(1);
        ArrayList<Node> predictedVRU = Model.getPredictionList(VRU);

        ArrayList<Node> test = new ArrayList<>();

        for(int i = 0; i < 10; i++)
            test.add(VRU.get(i));

        double[] testPoly = Model.predictionPolynomialRegression(test, 3, 0.5);

        System.out.println(test);
        System.out.println("\n" + "Predicted position, timestep 11: \n" + "x: " + testPoly[0] + " y: " + testPoly[1]);



        /*
        ArrayList<ArrayList<Node>> listOfNodeLists = new ArrayList<>();
        listOfNodeLists.add(VRU);
        listOfNodeLists.add(predictedVRU);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(listOfNodeLists, 20.0);
            frame.setVisible(true);
        });
        */

        /*System.out.println(Model.positionalRMSE(VRU, 0.5, 4));
        double[] RMSEcord = Model.coordinateRMSE(VRU, 0.5, 4);
        System.out.println("x: " + RMSEcord[0] + ", " + "y: " + RMSEcord[1]);
        ArrayList<Node> test = new ArrayList<>();

        for(Node n : VRU) {
            test.add(n);
            System.out.println(Model.checkForChange(test, 2));
        }

        /*
        ArrayList<Node> VRU1 = whatToRead.getDataFor(1);
        ArrayList<Node> test = new ArrayList<>();
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for(Node n : VRU1) {
            xValues.add(n.getX());
            yValues.add(n.getY());
        }

        for(Node n : VRU1) {
            test.add(n);
            System.out.println(Model.checkForChange(test));
        }

        int [] timeStep = whatToRead.timeStepsFor(1);
        int time = timeStep[0];
        ArrayList<Double> newXValues = new ArrayList<>();
        ArrayList<Double> newYValues = new ArrayList<>();
        double[] newpos;
        for(Node n : VRU1) {
            test.add(n);
            newpos = Model.predictNextPosition(test);
            time++;
            System.out.println("Time step : " + time );
            System.out.print("Predicted xy: ");
            for(int i = 0; i < 2; i++) {
                System.out.print(newpos[i] + " ");
            }
            System.out.print("\nActual position: " + xValues.get(time-1) + " " + yValues.get(time-1));
            System.out.println("\n");
            newXValues.add(newpos[0]);
            newYValues.add(newpos[1]);
        }

        double RMSEx = 0.0;
        double RMSEy = 0.0;
        for(int i = 0; i < timeStep[1]-timeStep[0]+1; i++){
            RMSEx += Math.pow(xValues.get(i+1) - newXValues.get(i),2);
            RMSEy += Math.pow(yValues.get(i+1) - newYValues.get(i),2);
        }
        RMSEx = Math.sqrt(RMSEx / (timeStep[1] - timeStep[0]+1));
        RMSEy = Math.sqrt(RMSEy / (timeStep[1] - timeStep[0]+1));


        System.out.println(timeStep[1]-timeStep[0]);
        System.out.println("Old x values: " + xValues.size() + ", New x values: " + newXValues.size());
        System.out.println(RMSEx + ", " + RMSEy);
         */

    }
}
