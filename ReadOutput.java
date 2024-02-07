import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadOutput {

    double timePassed;
    private int id; //VRU id
    private double v; //velocity
    private double a; //acceleration
    private double xpos;
    private double ypos;
    private int nrOfVRUs;
    Scanner vsc;
    Scanner psc;
    Scanner csc;
    File vDataOutput;
    File pDataOutput;
    File cDataOutput;

    public ReadOutput(String outputPath) {

        vDataOutput = new File(outputPath + "\\velocity.txt");
        pDataOutput = new File(outputPath + "\\position.txt");
        cDataOutput = new File(outputPath + "\\nrPedAtSameTime.txt");
    }

    public int totalNrOfVRUs() throws FileNotFoundException {

        csc = new Scanner(cDataOutput);
        csc.nextLine();
        timePassed = (double) csc.nextInt() / 10;
        int currentNr;
        int previousNr = csc.nextInt();
        nrOfVRUs = previousNr;

        while(csc.hasNextInt()) {

            timePassed = csc.nextInt();
            currentNr = csc.nextInt();

            if(previousNr < currentNr)
                nrOfVRUs += currentNr - previousNr;

            previousNr = currentNr;
            csc.nextLine();
        }

        return nrOfVRUs;
    }

    public double meanVelocity(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();
        double vsum = 0;
        int count = 0;
        while(vsc.hasNext()) {

            timePassed = (double) vsc.nextInt() / 10;

            if(VRUid == vsc.nextInt()) {
                vsum += vsc.nextDouble();
                count++;
            }
            vsc.nextLine();
        }
        return vsum / count;
    }

    public double maxVelocity(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();
        double maxV = 0;
        double currentV;

        while(vsc.hasNext()) {

            timePassed = (double) vsc.nextInt() / 10;

            if(VRUid == vsc.nextInt())
                if((currentV = vsc.nextDouble()) > maxV)
                    maxV = currentV;

            vsc.nextLine();
        }
        return maxV;
    }

    public double velocityAtGivenTime(int VRUid, int timeStep) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();

        while(vsc.hasNext()) {

            if(timeStep == vsc.nextInt())
                if(VRUid == vsc.nextInt())
                    v = vsc.nextDouble();

            vsc.nextLine();
        }
        return v;
    }

    public double meanAcceleration(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();
        int count = 0;
        double previousV = 0;
        double currentV;
        double asum = 0;

        while(vsc.hasNext()) {

            timePassed = (double) vsc.nextInt() / 10;
            if(VRUid == vsc.nextInt())
                break;
            vsc.nextLine();
        }

        vsc.nextLine();

        while(vsc.hasNext()) {

            timePassed = (double) vsc.nextInt() / 10;

            if(VRUid == vsc.nextInt()) {
                currentV = vsc.nextDouble();
                asum += (currentV - previousV) / 0.1;
                previousV = currentV;
                count++;
            }
            vsc.nextLine();
        }
        return asum / count;
    }

    public double maxAcceleration(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();
        double maxA = 0;
        double previousV = 0;
        double currentV;
        double currentA;


        while(vsc.hasNext()) {

            timePassed = (double) vsc.nextInt() / 10;

            if(VRUid == vsc.nextInt()) {
                currentV = vsc.nextDouble();
                if((currentA = (currentV - previousV) / 0.1) > maxA)
                    maxA = currentA;
                previousV = currentV;
            }
            vsc.nextLine();
        }
        return maxA;
    }

    public double accelerationAtGivenTime(int VRUid, int timeStep) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.nextLine();
        double previousV = 0;

        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();

            if(timeStep-1 == timePassed)
                if (VRUid == vsc.nextInt())
                    previousV = vsc.nextDouble();

            if(timeStep == timePassed)
                if (VRUid == vsc.nextInt())
                    a = (vsc.nextDouble() - previousV) / 0.1;

            vsc.nextLine();
        }
        return a;
    }
}
