import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
        public static void main(String[] args) throws FileNotFoundException {

            String filePath = "C:\\Users\\ruben\\OneDrive\\Dokument\\Högskolan\\DCTVT24\\EXAMENS ARBETE\\Simulation\\Simulations\\Test\\output\\Curve_2024-02-20_15-48-11.782";
            ReadOutput whatToRead = new ReadOutput(filePath);

            ArrayList<Double> RMSEforDifVRUs = new ArrayList<>();
            StringBuilder output = new StringBuilder("pedestrianID no.dataPoints minRMSE\n");
            double minRMSE;
            int totNrVRUs = whatToRead.totalNrOfVRUs();

            for(int i = 1; i <= totNrVRUs; i++) {

                ArrayList<Node> VRU = whatToRead.getDataFor(i);
                System.out.println("pedestrianID: " + i);
                output.append(i).append(" ");

                for(int j = 1; j < 10; j++) {
                    RMSEforDifVRUs.add(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, 0.5, j, 1)));
                }

                minRMSE = Collections.min(RMSEforDifVRUs);
                System.out.println("No. data points: " + (1+RMSEforDifVRUs.indexOf(minRMSE)));
                output.append((1+RMSEforDifVRUs.indexOf(minRMSE))).append(" ");
                System.out.println("Min RMSE: " + minRMSE);
                output.append(Collections.min(RMSEforDifVRUs)).append("\n");
                RMSEforDifVRUs.clear();
                VRU.clear();
            }

            try {
                FileWriter writer = new FileWriter("result.txt");
                writer.write(String.valueOf(output));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


/*
            for (int i = 1; i < VRU.size(); i++)
                System.out.println(VRU.get(i) + " " + newpredictionVRU.get(i-1));

            System.out.println(Model.positionalRMSE(VRU,oldpredictionVRU));
            System.out.println(Model.positionalRMSE(VRU,newpredictionVRU));


 */
/*
            ArrayList<ArrayList<Node>> listOfNodeLists = new ArrayList<>();
            listOfNodeLists.add(whatToRead.getDataFor(10));
            listOfNodeLists.add(Model.getPredictionList(listOfNodeLists.get(0),0.5,3,1));

            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(listOfNodeLists, 20.0);
                frame.setVisible(true);
            });
            */

    }
}
