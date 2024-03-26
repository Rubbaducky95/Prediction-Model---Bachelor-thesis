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

        String filePath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\Simulations\\Test\\output\\";
        String fileName = "S-turn_0.9s_50VRUs";
        ReadOutput whatToRead = new ReadOutput(filePath + fileName);

        //ToTxt.differenceListForAllVRUs(whatToRead,0.9,3,1);
        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,2,1,1);
        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,3,1,1);
        //ToTxt.predictedPositionsForVRU(whatToRead,0.9,4,1,1);
        //ToTxt.actualPositionsForVRU(whatToRead,1);
        //ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,2,1);
        ToTxt.minMaxRMSEforDataPoints(whatToRead,0.9,2,6,1);

        /*ArrayList<Node> VRU = whatToRead.getDataFor(1);

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
