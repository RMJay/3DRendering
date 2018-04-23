package Graphics;

import java.awt.*;

public class Line2DNormalForm {

    final double[] homos;

    private Line2DNormalForm(double[] homos) {
        this.homos = homos;
    }

    public int getX() {
        return (int)homos[0];
    }

    public int getY() {
        return (int)homos[1];
    }

    public int getW() {
        return (int)homos[2];
    }

    static Line2DNormalForm lineThrough(Point p1, Point p2) {
        double[] p1Homogenous = new double[] { p1.x, p1.y, 1 };
        double[] p2Homogenous = new double[] { p2.x, p2.y, 1 };
        return new Line2DNormalForm(normalized(crossProduct(p1Homogenous, p2Homogenous)));
    }

    private static double[] crossProduct(double[] a, double[] b) {
        double[] c = new double[3];
        c[0] = a[1]*b[2] - a[2]*b[1];
        c[1] = a[2]*b[0] - a[0]*b[2];
        c[2] = a[0]*b[1] - a[1]*b[0];
        return c;
    }

    private static double[] normalized(double[] v) {
        double norm = Math.sqrt(v[0]*v[0] + v[1]*v[1]);
        double[] result = new double[3];
        result[0] = v[0]/norm;
        result[1] = v[1]/norm;
        result[2] = v[2]/norm;
        return result;
    }
}
