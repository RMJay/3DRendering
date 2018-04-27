package Graphics;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class Triangle3D {

    public final TriangleLabel label;
    private final Point3D[] points;
    private final Vector3D averageNormal;
    private final Vector3D[] normals;
    private final Color[] colors;

    public Triangle3D(Point3D v1, Point3D v2, Point3D v3, Vector3D averageNormal, TriangleLabel label, Color c1, Color c2, Color c3) {
        Point3D[] points = new Point3D[3];
        points[0] = v1;
        points[1] = v2;
        points[2] = v3;
        this.points = points;

        this.averageNormal = averageNormal;
        Vector3D[] normals = new Vector3D[3];
        normals[0] = averageNormal;
        normals[1] = averageNormal;
        normals[2] = averageNormal;
        this.normals = normals;

        this.label = label;

        Color[] colors = new Color[3];
        colors[0] = c1;
        colors[1] = c2;
        colors[2] = c3;
        this.colors = colors;
    }

    public Point3D getP(int i) {
        return points[i];
    }

    public Point3D getP1() {
        return points[0];
    }

    public Point3D getP2() {
        return points[1];
    }

    public Point3D getP3() {
        return points[2];
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
        return new Point3D(cX, cY, cZ, -1);
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
        return averageNormal;
    }

    public void setNorm(int i, Vector3D norm) {
        normals[i] = norm;
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

    public Triangle3D applying(AffineTransform3D transform) {
        if (transform != null) {
            return new Triangle3D(points[0].applying(transform), points[1].applying(transform), points[2].applying(transform),
                    averageNormal, label, colors[0], colors[1], colors[2]);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return String.format("Triangle3D{%s, %s, %s}", points[0].toString(), points[0].toString(), points[0].toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triangle3D that = (Triangle3D) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }
}
