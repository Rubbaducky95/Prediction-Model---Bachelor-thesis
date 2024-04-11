import java.util.ArrayList;

public class APFP {

    Node currPos;
    Node predPos;
    double posX;
    double posY;
    double negX;
    double negY;
    double angle;
    public APFP(Node currPos, Node predPos, double posX, double posY, double negX, double negY, double angle) {

        this.currPos = currPos;
        this.predPos = predPos;
        this. posX = posX;
        this.posY = posY;
        this.negX = negX;
        this.negY = negY;
        this.angle = angle;
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
