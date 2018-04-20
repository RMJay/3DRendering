package Graphics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class ModelReader {

    private FileReader fr;
    private StreamTokenizer in;

    public Model read(String shapeDataFilename, String textureDataFilename) {
        try {
            fr = new FileReader(shapeDataFilename);
            in = new StreamTokenizer(fr);

            ArrayList<Triangle> triangleAccumulator = new ArrayList<Triangle>();
            int lineNo = 0;
            while (in.ttype != in.TT_EOF) {
                Point3D v1 = readPoint();
                Point3D v2 = readPoint();
                Point3D v3 = readPoint();
                Triangle t = new Triangle(v1, v2, v3);
                //  System.out.println(String.format("%d: %s", lineNo, t.toString()));
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

    public Point3D readPoint() throws IOException {
        in.nextToken();
        int lineNo = in.lineno();
        double x = in.nval;
        in.nextToken();
        double y = in.nval;
        in.nextToken();
        double z = in.nval;
        return new Point3D(lineNo, x, y, z);
    }
}
