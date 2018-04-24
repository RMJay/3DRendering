package Graphics;

public class AffineTransform3D {
    final double[][] matrix;

    private AffineTransform3D(double[][] matrix) {
        this.matrix = matrix;
    }

    public static AffineTransform3D identity() {
        double[][] matrix = new double[4][4];
        for (int i = 0; i < 4; i++) {
            matrix[i][i] = 1.0;
        }
        return new AffineTransform3D(matrix);
    }

    public double getScaleX() {
        return matrix[0][0];
    }

    //==================================================================================================================

    public AffineTransform3D concatenateWith(AffineTransform3D other) {
        return new AffineTransform3D(crossProduct(other.matrix, matrix));
    }

    public AffineTransform3D applying(double[][] transformMatrix) {
        return new AffineTransform3D(crossProduct(transformMatrix, matrix));
    }

    public AffineTransform3D scaledBy(double scale) {
        double[][] s = new double[4][4];
        s[0][0] = scale;
        s[1][1] = scale;
        s[2][2] = scale;
        s[3][3] = 1.0;
        return this.applying(s);
    }

    public AffineTransform3D scaledBy(double scaleX, double scaleY, double scaleZ) {
        double[][] s = new double[4][4];
        s[0][0] = scaleX;
        s[1][1] = scaleY;
        s[2][2] = scaleZ;
        s[3][3] = 1.0;
        return this.applying(s);
    }

    public AffineTransform3D translatedBy(double tx, double ty, double tz) {
        double[][] tr = new double[4][4];
        for (int i = 0; i < 4; i++) {
            tr[i][i] = 1.0;
        }
        tr[3][0] = tx;
        tr[3][1] = ty;
        tr[3][2] = tz;
        return this.applying(tr);
    }

    public AffineTransform3D translatedBy(Point3D p) {
        return translatedBy(p.x, p.y, p.z);
    }

    public AffineTransform3D rotatedBy(double radX, double radY) {
        double[][] rX = new double[4][4];
        rX[0][0] = 1.0;
        rX[1][1] = Math.cos(radY);
        rX[2][1] = Math.sin(radY);
        rX[1][2] = -Math.sin(radY);
        rX[2][2] = Math.cos(radY);
        rX[3][3] = 1.0;

        double[][] rY = new double[4][4];
        rY[0][0] = Math.cos(radX);
        rY[0][2] = Math.sin(radX);
        rY[2][0] = -Math.sin(radX);
        rY[2][2] = Math.cos(radX);
        rY[1][1] = 1.0;
        rY[3][3] = 1.0;

        return this.applying(crossProduct(rX, rY));
    }

    ///=================================================================================================================

    static double[][] crossProduct(double[][] a, double[][] b) {
        double[][] transformed = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                transformed[i][j] = a[i][0]*b[0][j] + a[i][1]*b[1][j]
                        + a[i][2]*b[2][j]+ a[i][3]*b[3][j];
            }
        }
        return transformed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(matrix[i][j]);
                sb.append("   ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
