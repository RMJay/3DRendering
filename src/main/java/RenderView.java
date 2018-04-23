import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import Graphics.*;

public class RenderView extends JPanel {

    enum Mode { POLYGONS, LAMBERTIAN }

    private Mode mode = Mode.POLYGONS;
    private Scene scene = null;
    private Triangle2D[] triangles = null;
    private int[][] textureData = null;
    private Rectangle modelBounds = null;
    private AffineTransform3D transform = AffineTransform3D.identity().rotatedBy(Math.PI, 0.0);
    private AffineTransform centerAndScale = null;

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
        System.out.println(String.format("height=%d, width=%d", w, h));
        BufferedImage pixels = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        ZBuffer zBuffer = new ZBuffer(w, h);

        paintBackground(pixels);
        renderModel(pixels, zBuffer);
//        showBounds(g2d);
//        g.drawImage();
        g.drawImage(pixels,0 , 0, null);
    }

    void paintBackground(BufferedImage pixels) {
        int height = pixels.getHeight();
        int width = pixels.getWidth();

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
                pixels.setRGB(x, y, rgb);
            }
        }
    }

    //For testing
    public void refreshTriangles() {
        Point[] p = new Point[9];
        p[0] = new Point(100, 700);
        p[1] = new Point(700, 500);
        p[2] = new Point(200, 100);
        p[3] = new Point(120, 300);
        p[4] = new Point(390, 680);
        p[5] = new Point(720, 370);
        p[6] = new Point(250, 540);
        p[7] = new Point(520, 620);
        p[8] = new Point(500, 220);

        Triangle2D[] triangles = new Triangle2D[3];
        triangles[2] = new Triangle2D(p[6], p[7], p[8], 100, Colors.spring, null);
        triangles[1] = new Triangle2D(p[3], p[4], p[5], 200, Colors.grape, null);
        triangles[0] = new Triangle2D(p[0], p[1], p[2], 300, Colors.tangerine, null);
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

//    public void refreshTriangles() {
//        Iterator<Triangle3D> it = scene.getTriangles();
//
//        Triangle2D[] triangles = new Triangle2D[scene.numTriangles];
//        int i = 0;
//        Triangle2D triangle2D;
//        while (i < scene.numTriangles) {
//            if (mode == Mode.LAMBERTIAN) {
//                triangle2D = lambertianModeTriangle2D(it.next().applying(transform));
//            } else {
//                triangle2D = polygonModeTriangle2D(it.next().applying(transform));
//            }
//            triangles[i] = triangle2D;
//            i++;
//        }
//        Arrays.sort(triangles, Triangle2D.ZComparator);
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
//    }

//    Triangle2D polygonModeTriangle2D(Triangle3D t) {
//        Color fill;
//        Color stroke;
//        if (t.label == TriangleLabel.FACE) {
//            fill = Color.WHITE;
//            stroke = Color.RED;
//        } else {
//            fill = Colors.lemon;
//            stroke = Colors.tangerine;
//        }
//        return new Triangle2D(t, stroke, fill);
//    }
//
//    Triangle2D lambertianModeTriangle2D(Triangle3D t) {
//        Color fill;
//        Color stroke;
//        if (t.label == TriangleLabel.FACE) {
//            Point3D lightSource = scene.getLightSource().applying(transform);
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

    void renderModel(BufferedImage pixels, ZBuffer zBuffer) {
        for (Triangle2D t : triangles) {
            t.drawInto(pixels, zBuffer);
        }
    }

    public void showBounds(Graphics2D g) {
        if (modelBounds != null) {
            g.setColor(Color.PINK);
            g.drawRect(modelBounds.x, modelBounds.y, modelBounds.width, modelBounds.height);
        }
    }

    void centerAndScale() {
        refreshTriangles();
        centerAndScale = centerAndScaleBasedOn(modelBounds, getBounds());
    }

    AffineTransform centerAndScaleBasedOn(Rectangle modelBounds, Rectangle panelBounds) {
        double fw = 0.8 * (double)panelBounds.width / modelBounds.width;
        double fh = 0.8 * (double)panelBounds.height / modelBounds.height;

        double f = fw <= fh ? fw : fh;

        double tx = (double)panelBounds.width / 2.0;
        double ty = (double)panelBounds.height / 2.0;

        AffineTransform tr = new AffineTransform();
        tr.translate(tx, ty);
        tr.scale(f, -f);
        return tr;
    }

    void rotateBy(double radX, double radY) {
        transform = transform.rotatedBy(radX, radY);
        refreshTriangles();
        repaint();
    }

    void scrollBy(double deltaX, double deltaY) {
        double scale = centerAndScale.getScaleX();
        centerAndScale.translate(deltaX/scale, -deltaY/scale);
        repaint();
    }

    void zoomBy(double factor) {
        centerAndScale.scale(factor, factor);
        repaint();
    }

}
