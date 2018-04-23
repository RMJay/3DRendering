package Graphics;

import java.util.Comparator;

public class Triangle3D {

    public final TriangleLabel label;
    public final Point3D v1, v2, v3;
    public final Vector3D normal;

    public Triangle3D(Point3D v1, Point3D v2, Point3D v3, Vector3D normal, TriangleLabel label) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.normal = normal;
        this.label = label;
    }

    public double centroidZ() {
        return (v1.z + v2.z + v3.z) / 3.0;
    }

    public Point3D centroid() {
        double cX = (v1.x + v2.x + v3.x) / 3.0;
        double cY = (v1.y + v2.y + v3.y) / 3.0;
        double cZ = (v1.z + v2.z + v3.z) / 3.0;
        return new Point3D( cX, cY, cZ);
    }

    @Override
    public String toString() {
        return String.format("Triangle3D{%s, %s, %s}", v1.toString(), v2.toString(), v3.toString());
    }

    public Triangle3D applying(AffineTransform3D transform) {
        return new Triangle3D(v1.applying(transform), v2.applying(transform), v3.applying(transform), normal.applying(transform), label);
    }

}
