package Graphics;

import java.util.Comparator;

public class Triangle {
    public final Point3D v1, v2, v3;
    public final int g1, g2, g3;

    public Triangle(Point3D v1, Point3D v2, Point3D v3, int g1, int g2, int g3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    public String getId() {  //the line numbers of the three points "#_#_#"
        return String.format("%d_%d_%d", v1.id, v2.id, v3.id);
    };

    public double centroidZ() {
        return (v1.z + v2.z + v3.z) / 3.0;
    }

    @Override
    public String toString() {
        return String.format("Triangle{id: %s, %s, %s, %s}", getId(), v1.toString(), v2.toString(), v3.toString());
    }

    public static final Comparator<Triangle> ZCentroidComparator = new Comparator<Triangle>() {
        @Override
        public int compare(Triangle t1, Triangle t2) {
            double delta = t1.centroidZ() - t2.centroidZ();
            if (delta >= 0) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    Triangle applying(AffineTransform3D transform) {
        return new Triangle(v1.applying(transform), v2.applying(transform), v3.applying(transform), g1, g2, g3);
    }
}
