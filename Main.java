import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "C:\\Users\\ruben\\OneDrive" +
                "\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\Simulations\\Test\\output\\";
        String simulationPath = "Straight_line_2024-02-19_16-31-53.43";
        ReadOutput whatToRead = new ReadOutput(filePath + simulationPath);

        ArrayList<Node> VRU1 = whatToRead.getDataFor(1);
        ArrayList<Node> test = new ArrayList<>();
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for(Node n : VRU1) {
            xValues.add(n.getX());
            yValues.add(n.getY());
        }
        /*
        for(Node n : VRU1) {
            test.add(n);
            System.out.println(Model.checkForChange(test));
        }
        */

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
            for(int i = 0; i < 2; i++)
                System.out.println(newpos[i] + " ");
            System.out.println("\n");
            newXValues.add(newpos[0]);
            newYValues.add(newpos[1]);
        }

        double RMSEx = 0.0;
        double RMSEy = 0.0;
        for(int i = 0; i < timeStep[1]-timeStep[0]-1; i++){
            RMSEx += Math.pow(xValues.get(i+1) - newXValues.get(i),2);
            RMSEy += Math.pow(yValues.get(i+1) - newYValues.get(i),2);
        }
        RMSEx = Math.sqrt(RMSEx / (timeStep[1] - timeStep[0]));
        RMSEy = Math.sqrt(RMSEy / (timeStep[1] - timeStep[0]));

        System.out.println(RMSEx + ", " + RMSEy);

    }
}
