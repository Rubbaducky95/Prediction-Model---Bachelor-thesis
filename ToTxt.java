import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/*
* Functions for extracting the data as .txt files to use in MatLab
*/

public class ToTxt {

    public static void minRMSEforVRUs(ReadOutput vadereOutput, double t, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("ID dataPoints minRMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        double minRMSE;
        ArrayList<Double> RMSE = new ArrayList<>();

        for(int i = 1; i <= totNrVRUs; i++) {

            ArrayList<Node> VRU = vadereOutput.getDataFor(i);
            output.append(i).append(" ");
            for(int j = 1; j < 10; j++) {
            RMSE.add(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, j, flag)));
            }

        minRMSE = Collections.min(RMSE);
        output.append((1+RMSE.indexOf(minRMSE))).append(" ");
        output.append(minRMSE).append("\n");
        RMSE.clear();
        VRU.clear();
        }

        try {
        FileWriter writer = new FileWriter("result_minRMSE.txt");
        writer.write(String.valueOf(output));
        writer.close();
        } catch (
        IOException e) {
            e.printStackTrace();
        }
    }
    public static void maxRMSEforVRUs(ReadOutput vadereOutput, double t, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("ID dataPoints maxRMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        double maxRMSE;
        ArrayList<Double> RMSE = new ArrayList<>();

        for(int i = 1; i <= totNrVRUs; i++) {

            ArrayList<Node> VRU = vadereOutput.getDataFor(i);
            output.append(i).append(" ");
            for(int j = 1; j < 10; j++) {
                RMSE.add(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, j, flag)));
            }

            maxRMSE = Collections.max(RMSE);
            output.append((1+RMSE.indexOf(maxRMSE))).append(" ");
            output.append(maxRMSE).append("\n");
            RMSE.clear();
            VRU.clear();
        }

        try {
            FileWriter writer = new FileWriter("result_maxRMSE.txt");
            writer.write(String.valueOf(output));
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
    public static void RMSEforVRUsWithSpecDataPoints(ReadOutput vadereOutput, double t, int noDataPoints, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("ID no.DataPoints RMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        double RMSE;

        for (int i = 1; i <= totNrVRUs; i++) {

            ArrayList<Node> VRU = vadereOutput.getDataFor(i);
            output.append(i).append(" ");
            RMSE = Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, noDataPoints, flag));
            output.append(noDataPoints).append(" ");
            output.append(RMSE).append("\n");
            VRU.clear();
        }
        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath;
        if (flag == 1)
            filePath = directoryPath + "result_RMSE_allVRUs_AutoDPs.txt";
        else
            filePath = directoryPath + "result_RMSE_allVRUs_" + noDataPoints + "DPs.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void minMaxRMSEforVRUsWithSpecDataPoints(ReadOutput vadereOutput, double t, int noDataPoints, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("minRMSE maxRMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        ArrayList<Double> RMSE = new ArrayList<>();

        for (int i = 1; i <= totNrVRUs; i++) {

            ArrayList<Node> VRU = vadereOutput.getDataFor(i);
            RMSE.add(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, noDataPoints, flag)));
            VRU.clear();
        }

        output.append(Collections.min(RMSE)).append(" ");
        output.append(Collections.max(RMSE)).append("\n");

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath;
        if (flag == 1)
            filePath = directoryPath + "result_minMax_RMSE_allVRUs_AutoDPs.txt";
        else
            filePath = directoryPath + "result_minMax_RMSE_allVRUs_" + noDataPoints + "DPs.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void minMaxRMSEforVRUsWithMeanPrediction(ReadOutput vadereOutput, double t, int noDataPoints) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("minRMSE maxRMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        ArrayList<Double> RMSE = new ArrayList<>();

        for (int i = 1; i <= totNrVRUs; i++) {

            ArrayList<Node> VRU = vadereOutput.getDataFor(i);
            RMSE.add(Model.positionalRMSE(VRU, Model.getMeanPredictionList(VRU,t,noDataPoints)));
            VRU.clear();
        }

        output.append(Collections.min(RMSE)).append(" ");
        output.append(Collections.max(RMSE)).append("\n");

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath;

        filePath = directoryPath + "result_minMax_RMSE_MeanPred_" + noDataPoints + "DPs.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void minMaxRMSEforDataPoints(ReadOutput vadereOutput, double t, int lowerLimitDP, int upperLimitDP, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("no.DataPoints minRMSE maxRMSE\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        ArrayList<Double> RMSE = new ArrayList<>();

        for(int i = lowerLimitDP; i <= upperLimitDP; i++) {

            for (int j = 1; j <= totNrVRUs; j++) {

                ArrayList<Node> VRU = vadereOutput.getDataFor(j);
                RMSE.add(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, i, flag)));
                VRU.clear();
            }
            output.append(i).append(" ");
            output.append(Collections.min(RMSE)).append(" ");
            output.append(Collections.max(RMSE)).append("\n");
            RMSE.clear();
        }

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "result_minMax_RMSE_dataPoints.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void predictedPositionsForAllVRUs(ReadOutput vadereOutput, double t, int noDataPoints) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("timeStep VRUID x-position y-position\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        ArrayList<Node> predictedPositions = new ArrayList<>();

        for(int i = 1; i <= totNrVRUs; i++) {

            predictedPositions = Model.getPredictionList(vadereOutput.getDataFor(i),t,noDataPoints,1);

            for(Node n : predictedPositions) {

                output.append(n.timeStep).append(" ");
                output.append(n.id).append(" ");
                output.append(n.x).append(" ");
                output.append(n.y).append("\n");
            }
            predictedPositions.clear();
        }

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "result_predPos_allVRUs.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void predictedPositionsForVRU(ReadOutput vadereOutput, double t, int noDataPoints, int VRUid, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("timeStep VRUID x-position y-position\n");
        ArrayList<Node> predictedPositions = new ArrayList<>();

        predictedPositions = Model.getPredictionList(vadereOutput.getDataFor(VRUid),t,noDataPoints,flag);

        for(Node n : predictedPositions) {

            output.append(n.timeStep).append(" ");
            output.append(n.id).append(" ");
            output.append(n.x).append(" ");
            output.append(n.y).append("\n");
        }

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "result_predPos_" + noDataPoints + "DPs_" + "VRU" + VRUid + ".txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void predictedMeanPositionsForVRU(ReadOutput vadereOutput, double t, int noDataPoints, int VRUid) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("timeStep VRUID x-position y-position\n");
        ArrayList<Node> predictedPositions = Model.getMeanPredictionList(vadereOutput.getDataFor(VRUid),t, noDataPoints);

        for(Node n : predictedPositions) {

            output.append(n.timeStep).append(" ");
            output.append(n.id).append(" ");
            output.append(n.x).append(" ");
            output.append(n.y).append("\n");
        }

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "result_predMeanPos_from" + noDataPoints + "to2DPs_VRU" + VRUid + ".txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void actualPositionsForVRU(ReadOutput vadereOutput, int VRUid) throws FileNotFoundException {


        StringBuilder output = new StringBuilder("timeStep VRUID x-position y-position\n");
        ArrayList<Node> actualPositions = vadereOutput.getDataFor(VRUid);

        for(Node n : actualPositions) {

            output.append(n.timeStep).append(" ");
            output.append(n.id).append(" ");
            output.append(n.x).append(" ");
            output.append(n.y).append("\n");
        }

        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "result_actualPos_VRU" + VRUid + ".txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void differenceListForAllVRUs(ReadOutput vadereOutput, double t, int noDataPoints, int flag) throws FileNotFoundException {

        StringBuilder output = new StringBuilder("Differences in distance:\n");
        int totNrVRUs = vadereOutput.totalNrOfVRUs();
        double distanceBetween;

        for(int i = 0; i < totNrVRUs; i++) {
            ArrayList<Node> actualPos = vadereOutput.getDataFor(i);
            ArrayList<Node> predictedPos = Model.getPredictionList(actualPos,t,noDataPoints,flag);
            for(int j = 0; j < actualPos.size(); j++) {
                if((distanceBetween = actualPos.get(j).distanceBetween(predictedPos.get(j))) != 0)
                    output.append(distanceBetween).append("\n");
            }
        }
        String directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\";
        // Ensure the directory path ends with a file separator (e.g., '/')
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;
        }

        // Create the directory if it does not exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "differenceInPos.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(String.valueOf(output));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
