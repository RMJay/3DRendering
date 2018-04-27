package Graphics;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelReader {

    private FileReader frShape;
    private FileReader frTexture;
    private StreamTokenizer inShape;
    private StreamTokenizer inTexture;

//    private HashMap<Integer, Color> perVertexRGB = new HashMap<Integer, Color>();

    public Face read(String shapeDataFilename, String textureDataFilename) {
        try {
            frShape = new FileReader(shapeDataFilename);
            inShape = new StreamTokenizer(frShape);

            frTexture = new FileReader(textureDataFilename);
            inTexture = new StreamTokenizer(frTexture);

            ArrayList<Triangle3D> triangleAccumulator = new ArrayList<Triangle3D>();
            while (inShape.ttype != StreamTokenizer.TT_EOF) {
                for (int j = 0; j < 3; j++) {
                    inShape.nextToken();
                    inTexture.nextToken();
                }
                Point3D v1 = readPoint();
                Point3D v2 = readPoint();
                Point3D v3 = readPoint();
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
            }

            Triangle3D[] triangles = new Triangle3D[triangleAccumulator.size()];

            triangles = triangleAccumulator.toArray(triangles);

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
        return new Point3D(x, y, z);
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
