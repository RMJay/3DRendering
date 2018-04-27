package Graphics;

public class Point3D {
    public final double x, y, z;
    private int id;

    public Point3D(double x, double y, double z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public int intX() {
        return (int)Math.round(x);
    }

    public int intY() {
        return (int)Math.round(y);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Point{x:%g, y:%g, z:%g}", x, y, z);
    }

    public Point3D applying(AffineTransform3D transform) {
        final double[][] a = transform.matrix;
        final double[] h = { x, y, z, 1.0 }; //homogenous form
        double xNew = h[0]*a[0][0] + h[1]*a[1][0] + h[2]*a[2][0] + h[3]*a[3][0];
        double yNew = h[0]*a[0][1] + h[1]*a[1][1] + h[2]*a[2][1] + h[3]*a[3][1];
        double zNew = h[0]*a[0][2] + h[1]*a[1][2] + h[2]*a[2][2] + h[3]*a[3][2];
        return new Point3D(xNew, yNew, zNew, id);
    }
}
