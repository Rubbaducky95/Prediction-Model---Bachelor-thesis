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

        String filePath = "C:\\Users\\ruben\\OneDrive\\Dokument\\Högskolan\\DCTVT24\\EXAMENS ARBETE\\Simulation\\Simulations\\Test\\output\\";
        String fileName = "Z-turn_0.9s_100VRUs";
        ReadOutput whatToRead = new ReadOutput(filePath + fileName);

        ToTxt.actualPositionsForVRU(whatToRead,3);
        ToTxt.APFPForVRU(whatToRead,0.9,3,3);

        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,2,1,0);
        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,3,1,0);
        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,0,1,1);

        /*ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,2,0);
        ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,3,0);
        ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,0,1);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead,0.9);*/

        /*ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,2,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,3,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,4,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,5,4);*/

        /*ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 2);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 3);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 4);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 5);*/

//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,2,0);
//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,3,0);
//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,0,1);
        //ToTxt.differenceListForAllVRUs(whatToRead,0.9,3,1);
        //ToTxt.minMaxRMSEforDataPoints(whatToRead,0.9,2,6,1);

        /*ArrayList<Node> VRU = whatToRead.getDataFor(2);

        Node n1 = VRU.get(0);
        Node n2 = VRU.get(1);

        ArrayList<Double> angles = new ArrayList<>();

        for (int i = 2; i < VRU.size(); i++) {
            angles.add(VRU.get(i).angleBetween(n1,n2));
            n1 = VRU.get(i-1);
            n2 = VRU.get(i);
        }

        ArrayList<Node> test = new ArrayList<>();
        ArrayList<Integer> changeFlags = new ArrayList<>();

        for(Node n : VRU) {
            test.add(n);
            changeFlags.add(Model.checkForChange(test));
        }
        System.out.println(angles);
        System.out.println(changeFlags);*/

//        ArrayList<ArrayList<Node>> listOfNodeLists = new ArrayList<>();
//        listOfNodeLists.add(whatToRead.getDataFor(2));
//        listOfNodeLists.add(Model.getPredictionList(listOfNodeLists.get(0),0.9,0,1));
//
//        SwingUtilities.invokeLater(() -> {
//            MainFrame frame = new MainFrame(listOfNodeLists, 20.0);
//            frame.setVisible(true);
//        });


    }
}
