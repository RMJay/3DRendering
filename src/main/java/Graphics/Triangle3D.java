package Graphics;

import java.awt.*;
import java.util.Comparator;

public class Triangle3D {

    public final TriangleLabel label;
    public final Point3D[] points;
    public final Vector3D[] normals;
    public final Color[] colors;

    public Triangle3D(Point3D v1, Point3D v2, Point3D v3, Vector3D n1, Vector3D n2, Vector3D n3, TriangleLabel label, Color c1, Color c2, Color c3) {
        Point3D[] points = new Point3D[3];
        points[0] = v1;
        points[1] = v2;
        points[2] = v3;
        this.points = points;

        Vector3D[] 

        this.normals = normals;
        this.label = label;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public Point2D get2DPoint1() {
        return new Point2D(v1.x, v1.y);
    }

    public Point2D get2DPoint2() {
        return new Point2D(v2.x, v2.y);
    }

    public Point2D get2DPoint3() {
        return new Point2D(v3.x, v3.y);
    }

    public double getCentroidZ() {
        return (v1.z + v2.z + v3.z) / 3.0;
    }

    public Point3D getCentroid() {
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
        if (transform != null) {
            return new Triangle3D(v1.applying(transform), v2.applying(transform), v3.applying(transform), normal, label, c1, c2, c3);
        } else {
            return this;
        }
    }

}
