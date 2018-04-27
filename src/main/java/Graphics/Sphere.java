package Graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Sphere {

    static int N = 10;
    Triangle3D[] triangles;

    public Sphere(double r) {

        Point3D[][] points = new Point3D[N+1][2*N];

        for (int i = 0; i <= N; i++) {
            double theta = i * Math.PI / N;
            for (int j = 0; j < (2*N); j++) {
                double phi = j * Math.PI / N;
                double x = r * Math.sin(theta) * Math.cos(phi);
                double y = r * Math.sin(theta) * Math.sin(phi);
                double z = r * Math.cos(theta);
                points[i][j] = new Point3D(x, y, z, -1);
            }
        }

        Triangle3D[] triangles = new Triangle3D[4*N*(N-1)];
        int k = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < 2*N; j++) {
                Point3D v1 = points[i][j%(2*N)];
                Point3D v2 = points[i][(j+1)%(2*N)];
                Point3D v3 = points[i-1][j%(2*N)];
                Point3D v4 = points[i-1][(j+1)%(2*N)];
                if (i < N) {
                    Vector3D v1toV3 = Vector3D.vectorFromTo(v1, v3);
                    Vector3D v1toV2 = Vector3D.vectorFromTo(v1, v2);
                    Vector3D normal = Vector3D.crossProductAndNormalise(v1toV2, v1toV3);
                    Color c = new Color(0,0,0);
                    triangles[k] = new Triangle3D(v1, v2, v3, normal, TriangleLabel.LIGHT, c, c, c);
                    k++;
                }
                if (i > 1) {
                    Vector3D v2toV3 = Vector3D.vectorFromTo(v2, v3);
                    Vector3D v2toV4 = Vector3D.vectorFromTo(v2, v4);
                    Vector3D normal = Vector3D.crossProductAndNormalise(v2toV4, v2toV3);
                    Color c = new Color(0,0,0);
                    triangles[k] = new Triangle3D(v2, v4, v3, normal, TriangleLabel.LIGHT, c, c, c);
                    k++;
                }
            }
        }
        this.triangles = triangles;
    }




    public Iterator<Triangle3D> getTriangles() {
        return new Iterator<Triangle3D>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < triangles.length;
            }

            @Override
            public Triangle3D next() {
                Triangle3D next = triangles[i];
                i++;
                return next;
            }
        };
    }
}
