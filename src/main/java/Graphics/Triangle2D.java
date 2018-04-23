package Graphics;

import java.awt.*;
import java.util.Comparator;

public class Triangle2D {

    public final Polygon polygon;
    public final double z;
    public final Color stroke;
    public final Color fill;

    public Triangle2D(Triangle3D t, Color stroke, Color fill) {
        int numPoints = 3;
        int[] xPoints = { t.v1.intX(), t.v2.intX(), t.v3.intX() };
        int[] yPoints = { t.v1.intY(), t.v2.intY(), t.v3.intY() };
        polygon = new Polygon(xPoints, yPoints, numPoints);
        z = t.getCentroidZ();
        this.stroke = stroke;
        this.fill = fill;
    }

    public static final Comparator<Triangle2D> ZComparator = new Comparator<Triangle2D>() {
        @Override
        public int compare(Triangle2D t1, Triangle2D t2) {
            if (t1 == null || t2 == null) {
                System.out.println("why is there a null in here?");
                return 0;
            }
            double delta = t1.z - t2.z;
            if (delta > 0) {
                return -1;
            } else if (delta < 0) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    public Rectangle getBounds() {
        return polygon.getBounds();
    }

    public void paintPolygon(Graphics2D g) {
        g.setColor(fill);
        g.fillPolygon(polygon);
        if (stroke != null) {
            g.setColor(stroke);
            g.drawPolygon(polygon);
        }
    }
}
