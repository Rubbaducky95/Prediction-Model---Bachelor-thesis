import java.util.ArrayList;
import java.util.Collections;

public class APFP {

    Node currPos;
    Node predPos;
    double posX;
    double posY;
    double negX;
    double negY;
    double angle;
    String direction;
    public APFP(Node currPos, Node predPos, double posX, double posY, double negX, double negY, double angle, String direction) {

        this.currPos = currPos;
        this.predPos = predPos;
        this. posX = posX;
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

    public double getMinX() {

        ArrayList<Double> xList = new ArrayList<>();
        xList.add(this.currPos.x);
        xList.add(this.predPos.x);
        xList.add(this.posX);
        xList.add(this.negX);
        return Collections.min(xList);
    }

    public double getMinY() {

        ArrayList<Double> yList = new ArrayList<>();
        yList.add(this.currPos.y);
        yList.add(this.predPos.y);
        yList.add(this.posY);
        yList.add(this.negY);
        return Collections.min(yList);
    }

    public double getMaxX() {

        ArrayList<Double> xList = new ArrayList<>();
        xList.add(this.currPos.x);
        xList.add(this.predPos.x);
        xList.add(this.posX);
        xList.add(this.negX);
        return Collections.max(xList);
    }

    public double getMaxY() {

        ArrayList<Double> yList = new ArrayList<>();
        yList.add(this.currPos.y);
        yList.add(this.predPos.y);
        yList.add(this.posY);
        yList.add(this.negY);
        return Collections.max(yList);
    }

    public boolean contains(Node n) {

        if(this.direction == "Straight")
            return true;
        ArrayList<Node> polygon = new ArrayList<>();
        polygon.add(currPos);
        polygon.add(predPos);
        polygon.add(new Node(0,0,posX,posY,0));
        polygon.add(new Node(0,0,negX,negY,0));

        return pointInPolygon.isInside(polygon, n);
    }
}
