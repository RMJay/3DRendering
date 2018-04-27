package Graphics;

import java.util.Iterator;

public class Scene {

    final Face face;
    final Sphere light;
    private Point3D lightSource;
    public final int numTriangles;

    public Scene(Face face) {
        this.face = face;
        light = new Sphere(8000);
        lightSource = new Point3D(100000.0, 100000.0, 200000.0, -1);
        numTriangles = face.triangles.length + light.triangles.length;
    }

    public Point3D getLightSource() {
        return new Point3D(lightSource.x, lightSource.y, lightSource.z, -1);
    }

    public Iterator<Triangle3D> getTriangles() {
        return new Iterator<Triangle3D>() {
            Iterator<Triangle3D> faceTrianglesIterator = face.getTriangles();
            Iterator<Triangle3D> lightTrianglesIterator = light.getTriangles();
            AffineTransform3D lightTransform = AffineTransform3D.identity().translatedBy(lightSource);

            @Override
            public boolean hasNext() {
                return faceTrianglesIterator.hasNext() ||
                         lightTrianglesIterator.hasNext();
            }

            @Override
            public Triangle3D next() {
                if (faceTrianglesIterator.hasNext()) {
                    return faceTrianglesIterator.next();
                }
                if (lightTrianglesIterator.hasNext()) {
                    return lightTrianglesIterator.next().applying(lightTransform);
                }
                return null;
            }
        };
    }

    public void moveLightBy(Vector3D displacement) {
        double x = lightSource.x + displacement.dx;
        double y = lightSource.y + displacement.dy;
        double z = lightSource.z + displacement.dz;
        lightSource = new Point3D(x, y, z, -1);
    }

}
