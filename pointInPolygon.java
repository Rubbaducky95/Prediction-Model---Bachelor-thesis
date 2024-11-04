import java.util.ArrayList;

public class pointInPolygon {

    public static boolean onSegment(Node a, Node b, Node c) {

        return b.x <= Math.max(a.x, c.x) && b.x >= Math.min(a.x, c.x)
                && b.y <= Math.max(a.y, c.y) && b.y >= Math.min(a.y, c.y);
    }

    public static int orientation(Node p, Node q, Node r)
    {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0)
            return 0;
        return (val > 0) ? 1 : 2;
    }

    public static boolean doIntersect(Node p1, Node q1, Node p2, Node q2)
    {

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;

        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;

        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;

        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;

        return false;
    }

    public static boolean isInside(ArrayList<Node> polygon, Node p)
    {
        int INF = 10000;

        Node extreme = new Node(0,0,INF, p.y, 0);

        int count = 0, i = 0;
        do
        {
            int next = (i + 1) % polygon.size();
            if (doIntersect(polygon.get(i), polygon.get(next), p, extreme))
            {
                if (orientation(polygon.get(i), p, polygon.get(next)) == 0)
                    return onSegment(polygon.get(i), p, polygon.get(next));

                count++;
            }
            i = next;
        } while (i != 0);

        return (count & 1) == 1;
    }
}
