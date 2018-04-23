package Graphics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class ModelReader {

    private FileReader frShape;
    private FileReader frTexture;
    private StreamTokenizer inShape;
    private StreamTokenizer inTexture;

    public Face read(String shapeDataFilename, String textureDataFilename) {
        try {
            frShape = new FileReader(shapeDataFilename);
            inShape = new StreamTokenizer(frShape);

            frTexture = new FileReader(textureDataFilename);
            inTexture = new StreamTokenizer(frTexture);

            ArrayList<Triangle3D> triangleAccumulator = new ArrayList<Triangle3D>();
            int lineNo = 0;
            while (inShape.ttype != inShape.TT_EOF) {
                Point3D v1 = readPoint();
                Point3D v2 = readPoint();
                Point3D v3 = readPoint();
                int g1 = readGreyscale();
                int g2 = readGreyscale();
                int g3 = readGreyscale();
                Vector3D v1toV3 = Vector3D.vectorFromTo(v1, v3);
                Vector3D v1toV2 = Vector3D.vectorFromTo(v1, v2);
                Vector3D normal = Vector3D.crossProductAndNormalise(v1toV3, v1toV2);
                Triangle3D t = new Triangle3D(v1, v2, v3, normal, TriangleLabel.FACE);
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
        int lineNo = inShape.lineno();
        double x = inShape.nval;
        inShape.nextToken();
        double y = inShape.nval;
        inShape.nextToken();
        double z = inShape.nval;
        return new Point3D(x, y, z);
    }

    int readGreyscale() throws IOException {
        inTexture.nextToken();
        Double val = inTexture.nval;
        return val.intValue();
    }
}
