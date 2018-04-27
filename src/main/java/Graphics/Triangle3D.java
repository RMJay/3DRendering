package Graphics;

import java.awt.*;
import java.util.Comparator;

public class Triangle3D {

    public final TriangleLabel label;
    private final Point3D[] points;
    private final Vector3D[] normals;
    private final Color[] colors;

    public Triangle3D(Point3D v1, Point3D v2, Point3D v3, Vector3D n1, Vector3D n2, Vector3D n3, TriangleLabel label, Color c1, Color c2, Color c3) {
        Point3D[] points = new Point3D[3];
        points[0] = v1;
        points[1] = v2;
        points[2] = v3;
        this.points = points;

        Vector3D[] normals = new Vector3D[3];
        normals[0] = n1;
        normals[1] = n2;
        normals[2] = n3;
        this.normals = normals;

        this.label = label;

        Color[] colors = new Color[3];
        colors[0] = c1;
        colors[1] = c2;
        colors[2] = c3;
        this.colors = colors;
    }

    public Point2D get2DPoint1() {
        return new Point2D(points[0].x, points[0].y);
    }

    public Point2D get2DPoint2() {
        return new Point2D(points[1].x, points[1].y);
    }

    public Point2D get2DPoint3() {
        return new Point2D(points[2].x, points[2].y);
    }

    public double getCentroidZ() {
        return (points[0].z + points[1].z + points[2].z) / 3.0;
    }

    public Point3D getCentroid() {
        double cX = (points[0].x + points[1].x + points[2].x) / 3.0;
        double cY = (points[0].y + points[1].y + points[2].y) / 3.0;
        double cZ = (points[0].z + points[1].z + points[2].z) / 3.0;
        return new Point3D( cX, cY, cZ);
    }

    public Vector3D getN1() {
        return normals[0];
    }

    public Vector3D getN2() {
        return normals[1];
    }

    public Vector3D getN3() {
        return normals[2];
    }

    public Vector3D getAverageNormal() {
        double dx = (normals[0].dx + normals[1].dx + normals[2].dx) / 3.0;
        double dy = (normals[0].dy + normals[1].dy + normals[2].dy) / 3.0;
        double dz = (normals[0].dz + normals[1].dz + normals[2].dz) / 3.0;
        return new Vector3D(dx, dy, dz);
    }

    public Color getC1() {
        return colors[0];
    }

    public Color getC2() {
        return colors[1];
    }

    public Color getC3() {
        return colors[2];
    }

    @Override
    public String toString() {
        return String.format("Triangle3D{%s, %s, %s}", points[0].toString(), points[0].toString(), points[0].toString());
    }

    public Triangle3D applying(AffineTransform3D transform) {
        if (transform != null) {
            return new Triangle3D(points[0].applying(transform), points[1].applying(transform), points[2].applying(transform),
                    normals[0], normals[1], normals[2], label, colors[0], colors[1], colors[2]);
        } else {
            return this;
        }
    }

}
