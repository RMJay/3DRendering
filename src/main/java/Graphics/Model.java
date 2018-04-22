package Graphics;

import java.util.Arrays;

public class Model {

    public final Triangle[] triangles;

    public Model(Triangle[] triangles) {
        Arrays.sort(triangles, Triangle.ZCentroidComparator);
        this.triangles = triangles;
    }

    public Model applying(AffineTransform3D transform) {
        Triangle[] transformed = new Triangle[triangles.length];
        for(int i = 0; i < triangles.length; i++) {
            transformed[i] = triangles[i].applying(transform);
        }
        return new Model(transformed);
    }

    public int[] getTextureDataForTriangle(int i) {
        return new int[]{triangles[i].g1, triangles[i].g2, triangles[i].g3};
    }

}
