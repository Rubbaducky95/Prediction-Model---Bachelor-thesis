import java.util.ArrayList;

public class APFP {

    VRU currPos;
    VRU predPos;
    double posX;
    double posY;
    double negX;
    double negY;
    double posX2;
    double posY2;
    double negX2;
    double negY2;
    double angle;
    String direction;

    public APFP(VRU currPos, VRU predPos, double posX, double posY, double negX, double negY, double angle, String direction) {

        this.currPos = currPos;
        this.predPos = predPos;
        this.posX = posX;
        this.posY = posY;
        this.negX = negX;
        this.negY = negY;
        this.angle = angle;
        this.direction = direction;
    }

    public double getArea() {

        return Math.PI * Math.pow(currPos.distanceBetween(predPos),2) * angle / 360;
    }

    public ArrayList<ArrayList<Double>> getEdgePoints() {

        ArrayList<ArrayList<Double>> edgePoints = new ArrayList<>();
        ArrayList<Double> posPoint = new ArrayList<>();
        ArrayList<Double> negPoint = new ArrayList<>();
        posPoint.add(posX);posPoint.add(posY);
        negPoint.add(negX);negPoint.add(negY);
        edgePoints.add(posPoint);edgePoints.add(negPoint);
        return edgePoints;
    }
}
