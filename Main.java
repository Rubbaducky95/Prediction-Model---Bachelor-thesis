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
        String fileName = "Z-turn_0.9s_100VRUs";
        ReadOutput whatToRead = new ReadOutput(filePath + fileName);
        ToTxt.APFPForVRU(whatToRead,0.9,2,1,1);

        /*double meanAccuracy = 0;
        int n = whatToRead.totalNrOfVRUs();

        for (int i = 1; i < n; i++) {
            ArrayList<APFP> apfp = Model.getAPFPList(whatToRead.getDataFor(i), 0.9, 3, 2);
            meanAccuracy += Model.getAccuracy(apfp);
        }
        System.out.println(meanAccuracy / n);*/

        //ToTxt.actualPositionsForVRU(whatToRead,2);
        //ToTxt.APFPForVRU(whatToRead,0.9,3,1,2);

        /*ArrayList<Node> actualPos = whatToRead.getDataFor(5);
        ArrayList<APFP> areas = Model.getAPFPList(actualPos, 0.9, 3, 0);

        System.out.println(Model.getAccuracy(areas));*/

        /*ToTxt.predictedPositionsForVRU(whatToRead,0.9,3,2,1);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,4,2);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,3,2);*/

        /*ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,2,0);
        ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,3,0);
        ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,4,0);
        ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,5,0);*/

        /*ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,2,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,3,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,4,4);
        ToTxt.predictedMeanPositionsForVRU(whatToRead,0.9,5,4);*/

        /*ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(whatToRead,0.9,3,1);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 3);
        ToTxt.minMaxRMSEforVRUsWithMeanPrediction(whatToRead, 0.9, 4);*/

//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,2,0);
//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,3,0);
//      ToTxt.RMSEforVRUsWithSpecDataPoints(whatToRead,0.9,0,1);
        //ToTxt.differenceListForAllVRUs(whatToRead,0.9,3,2);
        //ToTxt.differenceListForAllVRUs(whatToRead,0.9,4,2);
        //ToTxt.differenceListForAllVRUs(whatToRead,0.9,3,1);
        //ToTxt.minMaxRMSEforDataPoints(whatToRead,0.9,2,6,0);

    }
}
