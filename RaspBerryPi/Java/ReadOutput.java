import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
/*
* Handles all functions related to reading the data output from Vadere
* */
public class ReadOutput {

    private int timePassed;
    private int id; //VRU id
    private double v; //velocity
    private double a; //acceleration
    private double xpos;
    private double ypos;
    Scanner vsc;
    Scanner psc;
    Scanner csc;
    File vDataOutput;
    File pDataOutput;
    File cDataOutput;
    File eDataOutput;

    public ReadOutput(String outputPath) {

        vDataOutput = new File(outputPath + "/velocity.txt");
        pDataOutput = new File(outputPath + "/position.txt");
        cDataOutput = new File(outputPath + "/noOfEntities.txt");
        eDataOutput = new File(outputPath + "/evacTime.txt");
    }

    public int totalNrOfVRUs() throws FileNotFoundException {

        csc = new Scanner(cDataOutput);
        csc.useLocale(Locale.US);
        csc.nextLine();
        timePassed = csc.nextInt();
        int currentNr;
        int previousNr = csc.nextInt();
        int nrOfVRUs = previousNr;

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

    public int[] timeStepsFor(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.useLocale(Locale.US);
        vsc.nextLine();
        int startTime = vsc.nextInt(); //What timestep we start counting from.
        while(vsc.hasNext()){
            if(vsc.nextInt() != VRUid) {
                vsc.nextLine();
                startTime = vsc.nextInt();
            } else {
                vsc.nextLine();
                break;
            }
        }

        int noOftimeSteps = 0; //How many timesteps from the starting time.
        while(vsc.hasNext()) {
            timePassed = vsc.nextInt();
            if(vsc.nextInt() == VRUid) {
                noOftimeSteps++;
                vsc.nextLine();
            } else { vsc.nextLine(); }
        }
        return new int[]{startTime, noOftimeSteps};
    }

    public double meanVelocity(int VRUid) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.useLocale(Locale.US);
        vsc.nextLine();
        double vsum = 0;
        int count = 0;
        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();

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
        vsc.useLocale(Locale.US);
        vsc.nextLine();
        double maxV = 0;
        double currentV;

        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();

            if(VRUid == vsc.nextInt())
                if((currentV = vsc.nextDouble()) > maxV)
                    maxV = currentV;

            vsc.nextLine();
        }
        return maxV;
    }

    public double velocityAtGivenTime(int VRUid, int timeStep) throws FileNotFoundException {

        vsc = new Scanner(vDataOutput);
        vsc.useLocale(Locale.US);
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
        vsc.useLocale(Locale.US);
        vsc.nextLine();
        int count = 0;
        double previousV = 0;
        double currentV;
        double asum = 0;

        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();
            if(VRUid == vsc.nextInt())
                break;
            vsc.nextLine();
        }

        vsc.nextLine();

        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();

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
        vsc.useLocale(Locale.US);
        vsc.nextLine();
        double maxA = 0;
        double previousV = 0;
        double currentV;
        double currentA;


        while(vsc.hasNext()) {

            timePassed = vsc.nextInt();

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
        vsc.useLocale(Locale.US);
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

    public VRU getDataAt(int timeStamp, int VRUid) throws FileNotFoundException {

        psc = new Scanner(pDataOutput);
        psc.useLocale(Locale.US);
        v = velocityAtGivenTime(VRUid, timeStamp);
        psc.nextLine();


        while(psc.hasNext()) {

            if(psc.nextInt() == timeStamp) {
                if(psc.nextInt() == VRUid) {
                    xpos = psc.nextDouble();
                    ypos = psc.nextDouble();
                    break;
                }
            }
            psc.nextLine();
        }
        return new VRU(timeStamp, VRUid, xpos, ypos, v);
    }

    public ArrayList<VRU> getDataFor(int VRUid) throws FileNotFoundException {

        psc = new Scanner(pDataOutput);
        psc.useLocale(Locale.US);
        ArrayList<VRU> nodeList = new ArrayList<>();
        psc.nextLine();

        while(psc.hasNext()) {

            timePassed = psc.nextInt();

            if(psc.nextInt() == VRUid)
                nodeList.add(getDataAt(timePassed, VRUid));

            psc.nextLine();
        }

        return nodeList;
    }

    public ArrayList<VRU> getXDataAt(int timeStep, int noSteps, int VRUid) throws FileNotFoundException {

        ArrayList<VRU> dataAtEachStep = new ArrayList<>();
        for(int i = 0; i < noSteps; i++) {

            if(timeStep != 0) {
                dataAtEachStep.add(getDataAt(timeStep, VRUid));
                timeStep--;
            }
            else break;
        }
        return dataAtEachStep;
    }

    public double distanceBetween(VRU n1, VRU n2){

        return Math.sqrt(Math.pow(n2.x-n1.x,2)+Math.pow(n2.y-n1.y,2));
    }

    public double changeInAngle(ArrayList<VRU> n){

        return 0;
    }

    public double getAPFP(int VRUid, int timeStep, int possibleTurningRadius){

        return 0;
    }
}
