import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        ReadOutput whatToRead = new ReadOutput("C:\\Users\\ruben\\OneDrive" +
                "\\Dokument\\Högskolan\\DCTVT24" +
                "\\EXAMENS ARBETE\\Simulation\\Simulations" +
                "\\Test\\output" +
                "\\busshallplats_hogskolan_2024-02-07_17-19-56.579");

        System.out.println(whatToRead.meanAcceleration(1));
        System.out.println(whatToRead.maxAcceleration(1));
        System.out.println(whatToRead.accelerationAtGivenTime(1, 4));
    }
}