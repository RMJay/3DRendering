import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import Graphics.*;

public class RenderView extends JPanel {

    enum Mode { POLYGONS, FLAT, Z_BUFFER }

    private Mode mode = Mode.POLYGONS;
    private Scene scene = null;
    private Triangle2D[] triangles = null;
    private int[][] textureData = null;
    private Rectangle modelBounds = null;
    private AffineTransform3D rotation = AffineTransform3D.identity()
            .rotatedBy(Math.PI, 0.0)
            .scaledBy(1.0,-1.0,1.0);
    private AffineTransform3D zoom = AffineTransform3D.identity();
    private AffineTransform3D scroll = AffineTransform3D.identity();

    public void setFace(Face face) {
        this.scene = new Scene(face);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();
        MyContext context = new MyContext(w, h);
        paintBackground(context);
        renderModel(context);
        if (mode == Mode.Z_BUFFER) {
            context.drawZBuffer();
        }
        showSceneBounds(context);
        g.drawImage(context.pixels,0 , 0, null);
    }

    void paintBackground(MyContext context) {
        int height = context.getHeight();
        int width = context.getWidth();

        int topRed = Colors.orchid.getRed();
        int topGreen = Colors.orchid.getGreen();
        int topBlue = Colors.orchid.getBlue();

        int bottomRed = Colors.midnight.getRed();
        int bottomGreen = Colors.midnight.getGreen();
        int bottomBlue = Colors.midnight.getBlue();

        double deltaRed = (double)(bottomRed - topRed) / height;
        double deltaGreen = (double)(bottomGreen - topGreen) / height;
        double deltaBlue = (double)(bottomBlue - topBlue) / height;

        for (int y = 0; y < height; y++) {
            int red = topRed + (int)(deltaRed * y);
            int green = topGreen + (int)(deltaGreen * y);
            int blue = topBlue + (int)(deltaBlue * y);
            int rgb = red;
            rgb = (rgb << 8) + green;
            rgb = (rgb << 8) + blue;
            for (int x = 0; x < width; x ++) {
                context.pixels.setRGB(x, y, rgb);
            }
        }
    }

//    //For testing
//    public void refreshTriangles() {
//        Point2D[] p = new Point2D[9];
//        p[0] = new Point2D(100, 700);
//        p[1] = new Point2D(700, 500);
//        p[2] = new Point2D(200, 100);
//        p[3] = new Point2D(120, 300);
//        p[4] = new Point2D(390, 680);
//        p[5] = new Point2D(720, 370);
//        p[6] = new Point2D(250, 540);
//        p[7] = new Point2D(520, 620);
//        p[8] = new Point2D(500, 220);
//
//        Triangle2D[] triangles = new Triangle2D[3];
//        triangles[2] = new Triangle2D(p[6], p[7], p[8], 250, Colors.spring, null);
//        triangles[1] = new Triangle2D(p[3], p[4], p[5], 350, Colors.maraschino, null);
//        triangles[0] = new Triangle2D(p[0], p[1], p[2], 300, Colors.tangerine, null);
//        this.triangles = triangles;
//
//        Rectangle bounds = null;
//        for (int j = 0; j < triangles.length; j++) {
//            Rectangle b = triangles[j].getBounds();
//            if (bounds == null) {
//                bounds = b;
//            } else {
//                Rectangle.union(bounds, triangles[j].getBounds(), bounds);
//            }
//        }
//        modelBounds = bounds;
//        System.out.println(String.format("Model bounds=%s", modelBounds.toString()));
//    }

    public void refreshTriangles() {
        Iterator<Triangle3D> it = scene.getTriangles();

        AffineTransform3D transform = AffineTransform3D.identity()
                .concatenateWith(scroll)
                .concatenateWith(zoom)
                .concatenateWith(rotation);

        Triangle2D[] triangles = new Triangle2D[scene.numTriangles];
        int i = 0;
        Triangle2D triangle2D;
        while (i < scene.numTriangles) {
            Triangle3D transformed = it.next().applying(transform);
            if (mode == Mode.FLAT) {
                triangle2D = flatModeTriangle2D(transformed);
            } else {
                triangle2D = polygonModeTriangle2D(transformed);
            }
            triangles[i] = triangle2D;
            i++;
        }
        this.triangles = triangles;

        Rectangle bounds = null;
        for (int j = 0; j < triangles.length; j++) {
            Rectangle b = triangles[j].getBounds();
            if (bounds == null) {
                bounds = b;
            } else {
                Rectangle.union(bounds, triangles[j].getBounds(), bounds);
            }
        }
        modelBounds = bounds;
    }

    Triangle2D polygonModeTriangle2D(Triangle3D t) {
        Color fill;
        Color stroke;
        if (t.label == TriangleLabel.FACE) {
            fill = Color.WHITE;
            stroke = Color.RED;
        } else {
            fill = Colors.lemon;
            stroke = Colors.tangerine;
        }
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), fill, stroke);
    }

    Triangle2D flatModeTriangle2D(Triangle3D t) {
        Color fill;
        Color stroke;
        if (t.label == TriangleLabel.FACE) {
            Point3D lightSource = scene.getLightSource();
            Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
            Vector3D normal = t.normal;
            double dotProduct = Vector3D.dotProduct(directionToLightSource, t.normal);
            int greyscale = (int)Math.round(dotProduct * 255);
            if (greyscale < 0) {
                greyscale = 0;
            }
            fill = new Color(greyscale, greyscale, greyscale);
            stroke = null;
        } else {
            fill = Colors.lemon;
            stroke = Colors.tangerine;
        }
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), fill, stroke);
    }


    void renderModel(MyContext context) {
        Rectangle dirtyRect = new Rectangle(0, 0, getWidth(), getHeight());
        for (Triangle2D t : triangles) {
            t.drawInto(context);
        }
    }

    public void showSceneBounds(MyContext context) {
        Graphics2D g = context.pixels.createGraphics();
        if (modelBounds != null) {
            g.setColor(Color.PINK);
            g.drawRect(modelBounds.x, modelBounds.y, modelBounds.width, modelBounds.height);
        }
    }

    void centerAndScale() {
        if (modelBounds != null) {
            Rectangle panelBounds = getBounds();
            double fw = 0.8 * (double) panelBounds.width / modelBounds.width;
            double fh = 0.8 * (double) panelBounds.height / modelBounds.height;
            double f = fw <= fh ? fw : fh;
            zoom = zoom.scaledBy(f, f, f);

            double tx = (double) panelBounds.width / 2.0;
            double ty = (double) panelBounds.height / 2.0;
            scroll = AffineTransform3D.identity().translatedBy(tx, ty, 0.0);
        }
    }

    void rotateBy(double radX, double radY) {
        rotation = rotation.rotatedBy(radX, -radY);
        refreshTriangles();
        repaint();
    }

    void scrollBy(double deltaX, double deltaY) {
        scroll = scroll.translatedBy(deltaX, deltaY, 0);
        refreshTriangles();
        repaint();
    }

    void zoomBy(double factor) {
        zoom = zoom.scaledBy(factor);
        refreshTriangles();
        repaint();
    }

    public void moveLight(double dxScreen, double dyScreen) {
        System.out.println("move light");
        Vector3D displacementScreen = new Vector3D(dxScreen, dyScreen, 0.0);
        AffineTransform3D transform = AffineTransform3D.identity()
                .concatenateWith(rotation);
        Vector3D displacementModel = displacementScreen.applying(transform.inverse());
        scene.moveLightBy(displacementModel);
        refreshTriangles();
        repaint();
    }

}
