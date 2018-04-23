package Graphics;

public class ZBuffer {

    public double[][] zData;

    public ZBuffer(int width, int height) {
        zData = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zData[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    double getBufferedZ(int i, int j) {
        return zData[i][j];
    }

    void setZ(int i, int j, double z) {
        zData[i][j] = z;
    }

}
