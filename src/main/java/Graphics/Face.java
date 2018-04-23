package Graphics;

import java.util.Iterator;

public class Face {

    public final Triangle3D[] triangles;

    public Face(Triangle3D[] triangles) {
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
