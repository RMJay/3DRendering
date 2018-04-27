package Graphics;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ModelReader {

    private FileReader frShape;
    private FileReader frTexture;
    private StreamTokenizer inShape;
    private StreamTokenizer inTexture;

    private HashMap<Integer, HashSet<Triangle3D>> adjacencyList = new HashMap<Integer, HashSet<Triangle3D>>(); //vertexes and their adjacent triangles
    ArrayList<Triangle3D> triangleAccumulator = new ArrayList<Triangle3D>();
    Triangle3D[] triangles;

    public Face read(String shapeDataFilename, String textureDataFilename) {
        try {
            frShape = new FileReader(shapeDataFilename);
            inShape = new StreamTokenizer(frShape);

            frTexture = new FileReader(textureDataFilename);
            inTexture = new StreamTokenizer(frTexture);

            while (inShape.ttype != StreamTokenizer.TT_EOF) {
                int[] ids = new int[3];
                for (int i = 0; i < 3; i++) {
                    inShape.nextToken();
                    ids[i] = (int)inShape.nval;
                    inTexture.nextToken();
                }
                Point3D v1 = readPoint();
                v1.setId(ids[0]);
                Point3D v2 = readPoint();
                v2.setId(ids[1]);
                Point3D v3 = readPoint();
                v3.setId(ids[2]);
                Color c1 = new Color(100,100,100);
                Color c2 = new Color(100,100,100);
                Color c3 = new Color(100,100,100);
                try {
                    c1 = readColor();
                    c2 = readColor();
                    c3 = readColor();
                } catch (IllegalArgumentException e) {
                    //nothing
                }
                Vector3D v1toV3 = Vector3D.vectorFromTo(v1, v3);
                Vector3D v1toV2 = Vector3D.vectorFromTo(v1, v2);
                Vector3D normal = Vector3D.crossProductAndNormalise(v1toV3, v1toV2);
                Triangle3D t = new Triangle3D(v1, v2, v3, normal, TriangleLabel.FACE, c1, c2, c3);
                triangleAccumulator.add(t);

                HashSet<Triangle3D> set1 = adjacencyList.get(v1.getId());
                if (set1 == null) {
                    set1 = new HashSet<Triangle3D>();
                    adjacencyList.put(v1.getId(), set1);
                }
                set1.add(t);

                HashSet<Triangle3D> set2 = adjacencyList.get(v2.getId());
                if (set2 == null) {
                    set2 = new HashSet<Triangle3D>();
                    adjacencyList.put(v2.getId(), set2);
                }
                set2.add(t);

                HashSet<Triangle3D> set3 = adjacencyList.get(v3.getId());
                if (set3 == null) {
                    set3 = new HashSet<Triangle3D>();
                    adjacencyList.put(v3.getId(), set3);
                }
                set3.add(t);
            }

            triangles = new Triangle3D[triangleAccumulator.size()];
            triangles = triangleAccumulator.toArray(triangles);

            for(Triangle3D t: triangles) {
                int id;
                HashSet<Triangle3D> adj;
                double dx, dy, dz;
                Vector3D norm;

                for (int i = 0; i < 3; i++) {
                    id = t.getP(i).getId();
                    adj = adjacencyList.get(id);
                    dx = 0.0;
                    dy = 0.0;
                    dz = 0.0;
                    for (Triangle3D a : adj) {
                        Vector3D avNorm = a.getAverageNormal();
                        dx += avNorm.dx;
                        dy += avNorm.dy;
                        dz += avNorm.dy;
                    }
                    norm = new Vector3D(dx, dy, dz).normalized();
                    t.setNorm(i, norm);
                }

            }

            return new Face(triangles);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Point3D readPoint() throws IOException {
        inShape.nextToken();
        double x = inShape.nval;
        inShape.nextToken();
        double y = inShape.nval;
        inShape.nextToken();
        double z = inShape.nval;
        return new Point3D(x, y, z, -1);
    }

    Color readColor() throws IOException {
        inTexture.nextToken();
        int r = (int)inTexture.nval;
        inTexture.nextToken();
        int g = (int)inTexture.nval;
        inTexture.nextToken();
        int b = (int)inTexture.nval;
        Color color = new Color(r, g, b);
        return color;
    }
}
