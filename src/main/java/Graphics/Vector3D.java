package Graphics;

public class Vector3D {

    final double dx, dy, dz;

    Vector3D(double dx, double dy, double dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static Vector3D vectorFromTo(Point3D p1, Point3D p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double dz = p2.z - p1.z;
        return new Vector3D(dx, dy, dz);
    }

    public static Vector3D crossProductAndNormalise(Vector3D v1, Vector3D v2) {
        double dx = v1.dy * v2.dz - v1.dz * v2.dy;
        double dy = v1.dz * v2.dx - v1.dx * v2.dz;
        double dz = v1.dx * v2.dy - v1.dy * v2.dx;
        double norm = Math.sqrt(dx*dx + dy*dy + dz*dz);
        return new Vector3D(dx/norm, dy/norm, dz/norm);
    }

    Vector3D applying(AffineTransform3D transform) {
        final double[][] a = transform.matrix;
        final double[] h = { dx, dy, dz, 1.0 }; //homogenous form
        double dxNew = h[0]*a[0][0] + h[1]*a[1][0] + h[2]*a[2][0] + h[3]*a[3][0];
        double dyNew = h[0]*a[0][1] + h[1]*a[1][1] + h[2]*a[2][1] + h[3]*a[3][1];
        double dzNew = h[0]*a[0][2] + h[1]*a[1][2] + h[2]*a[2][2] + h[3]*a[3][2];
        return new Vector3D(dxNew, dyNew, dzNew);
    }

    public Vector3D normalized() {
        double norm = Math.sqrt(dx*dx + dy*dy + dz*dz);
        return new Vector3D(dx/norm, dy/norm, dz/norm);
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.dx * v2.dx + v1.dy*v2.dy + v1.dz*v2.dz;
    }

    @Override
    public String toString() {
        return String.format("{dx: %g, dy: %g, dz: %g}", dx, dy, dz);
    }
}
