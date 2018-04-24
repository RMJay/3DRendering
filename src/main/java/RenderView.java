import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import Graphics.*;

public class RenderView extends JPanel {

    enum Mode { POLYGONS, LAMBERTIAN }

    private Mode mode = Mode.POLYGONS;
    private Scene scene = null;
    private Triangle2D[] triangles = null;
    private int[][] textureData = null;
    private Rectangle modelBounds = null;
    private AffineTransform3D sceneTransform = AffineTransform3D.identity().rotatedBy(Math.PI, 0.0);
    private AffineTransform3D centerAndScale = null;

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
//        Point[] p = new Point[9];
//        p[0] = new Point(100, 700);
//        p[1] = new Point(700, 500);
//        p[2] = new Point(200, 100);
//        p[3] = new Point(120, 300);
//        p[4] = new Point(390, 680);
//        p[5] = new Point(720, 370);
//        p[6] = new Point(250, 540);
//        p[7] = new Point(520, 620);
//        p[8] = new Point(500, 220);
//
//        Triangle2D[] triangles = new Triangle2D[3];
//        triangles[2] = new Triangle2D(p[6], p[7], p[8], 100, Colors.spring, null);
//        triangles[1] = new Triangle2D(p[3], p[4], p[5], 200, Colors.maraschino, null);
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
        AffineTransform3D transform = AffineTransform3D.identity();
        transform.concatenateWith(sceneTransform);
        if (centerAndScale != null) {
            transform = transform.concatenateWith(centerAndScale);
        }

        Triangle2D[] triangles = new Triangle2D[scene.numTriangles];
        int i = 0;
        Triangle2D triangle2D;
        while (i < scene.numTriangles) {
            Triangle3D transformed = it.next().applying(transform);
            if (mode == Mode.LAMBERTIAN) {
                triangle2D = polygonModeTriangle2D(transformed);
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

//    Triangle2D lambertianModeTriangle2D(Triangle3D t) {
//        Color fill;
//        Color stroke;
//        if (t.label == TriangleLabel.FACE) {
//            Point3D lightSource = scene.getLightSource().applying(sceneTransform);
//            Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
//            Vector3D normal = t.normal;
//            double dotProduct = Vector3D.dotProduct(directionToLightSource, t.normal);
//            int greyscale = (int)Math.round(dotProduct * 255);
//            if (greyscale < 0) {
//                greyscale = 0;
//            }
//            fill = new Color(greyscale, greyscale, greyscale);
//            stroke = null;
//        } else {
//            fill = Colors.lemon;
//            stroke = Colors.tangerine;
//        }
//        return new Triangle2D(t, stroke, fill);
//    }

//    void renderModel(Graphics2D g, Mode mode) {
//        g.setTransform(centerAndScale);
//        for (Triangle2D t : triangles) {
//            t.paintPolygon(g);
//        }
//    }

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
        refreshTriangles();
        centerAndScale = centerAndScaleBasedOn(modelBounds, getBounds());
    }

    AffineTransform3D centerAndScaleBasedOn(Rectangle modelBounds, Rectangle panelBounds) {
        double fw = 0.8 * (double)panelBounds.width / modelBounds.width;
        double fh = 0.8 * (double)panelBounds.height / modelBounds.height;

        double f = fw <= fh ? fw : fh;

        double tx = (double)panelBounds.width / 2.0;
        double ty = (double)panelBounds.height / 2.0;

        return AffineTransform3D.identity().translatedBy(tx, ty, 0.0).scaledBy(f, -f, f);
    }

    void rotateBy(double radX, double radY) {
        sceneTransform = sceneTransform.rotatedBy(radX, radY);
        refreshTriangles();
        repaint();
    }

    void scrollBy(double deltaX, double deltaY) {
        double scale = centerAndScale.getScaleX();
        centerAndScale = centerAndScale.translatedBy(deltaX, deltaY, 0);
        refreshTriangles();
        repaint();
    }

    void zoomBy(double factor) {
        centerAndScale = centerAndScale.scaledBy(factor);
        refreshTriangles();
        repaint();
    }

}
