import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "C:\\Users\\ruben\\OneDrive" +
                "\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\Simulations\\Test\\output\\";
        String simulationPath = "Straight_line_2024-02-15_17-29-41.945";
        ReadOutput whatToRead = new ReadOutput(filePath + simulationPath);

        ArrayList<Node> n = whatToRead.getXDataAt(30,3,1);
        System.out.println(n);
        System.out.println(whatToRead.accelerationAtGivenTime(1,30));
        System.out.println(whatToRead.distanceBetween(n.get(0),whatToRead.getDataAt(31,1)));
    }
}
