package Graphics;

public class AffineTransform3D {

    private double[][] matrix;

    private AffineTransform3D(double[][] matrix) {
        this.matrix = matrix;
    }

    public static AffineTransform3D identity() {
        double[][] matrix = new double[4][4];
        matrix[0][0] = 1.0;
        matrix[1][1] = 1.0;
        matrix[2][2] = 1.0;
        matrix[3][3] = 1.0;
        return new AffineTransform3D(matrix);
    }

}
