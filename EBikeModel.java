public class EBikeModel {

    public static void main(String[] args) {
        //assumes constant acceleration and a single-motion mode.
        //varying acceleration and transitioning between different types of motion must be included.

        //Put in initial conditions manually from ReadOutput.java
        double x = 0.9910167814334254 ; // x position
        double y = 9.057447205358901; // y position
        double v = 0.5906222252228555; // Velocity
        double a = 5.906222252228554; // Acceleration
        double deltaT = 0.5; // Time step in seconds (doublecheck SimTimeStep parameter in Vadere)

        // Calculate new position
        double[] newPosition = updatePosition(x, y, v, a, deltaT);

        // Print new position
        System.out.println("New Position: (" + newPosition[0] + ", " + newPosition[1] + ")");
    }

    public static double[] updatePosition(double x, double y, double v, double a, double deltaT) {
        // Calculate radius based on current speed and acceleration
        double r = v * v / a;

        // Change in angular position
        double deltaTheta = v / r * deltaT;

        // Calculate new position
        double newX = x + r * Math.sin(deltaTheta);
        double newY = y + r * Math.cos(deltaTheta);

        // Return new position as an array
        return new double[]{newX, newY};
    }
}
