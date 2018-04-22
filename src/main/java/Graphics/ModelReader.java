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

    public Model read(String shapeDataFilename, String textureDataFilename) {
        try {
            frShape = new FileReader(shapeDataFilename);
            inShape = new StreamTokenizer(frShape);

            frTexture = new FileReader(textureDataFilename);
            inTexture = new StreamTokenizer(frTexture);

            ArrayList<Triangle> triangleAccumulator = new ArrayList<Triangle>();
            int lineNo = 0;
            while (inShape.ttype != inShape.TT_EOF) {
                Point3D v1 = readPoint();
                Point3D v2 = readPoint();
                Point3D v3 = readPoint();
                int g1 = readGreyscale();
                int g2 = readGreyscale();
                int g3 = readGreyscale();
                Triangle t = new Triangle(v1, v2, v3, g1, g2, g3);
                triangleAccumulator.add(t);
            }

            Triangle[] triangles = new Triangle[triangleAccumulator.size()];
            triangles = triangleAccumulator.toArray(triangles);

            return new Model(triangles);

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
        return new Point3D(lineNo, x, y, z);
    }

    int readGreyscale() throws IOException {
        inTexture.nextToken();
        Double val = inTexture.nval;
        return val.intValue();
    }
}
