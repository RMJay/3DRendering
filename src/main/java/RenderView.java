import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintBackground(g2d);
        renderModel(g2d, mode);
        showBounds(g2d);
    }

    void paintBackground(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color top = Colors.orchid;
        Color bottom = Colors.midnight;
        GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
        g.setPaint(gp);
        g.fillRect(0,0,w,h);
    }

    public void refreshTriangles() {
        Iterator<Triangle3D> it = scene.getTriangles();

        Triangle2D[] triangles = new Triangle2D[scene.numTriangles];
        int i = 0;
        Triangle2D triangle2D;
        while (i < scene.numTriangles) {
            if (mode == Mode.LAMBERTIAN) {
                triangle2D = lambertianModeTriangle2D(it.next().applying(transform));
            } else {
                triangle2D = polygonModeTriangle2D(it.next().applying(transform));
            }
            triangles[i] = triangle2D;
            i++;
        }
        Arrays.sort(triangles, Triangle2D.ZComparator);
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
        return new Triangle2D(t, stroke, fill);
    }

    Triangle2D lambertianModeTriangle2D(Triangle3D t) {
        Color fill;
        Color stroke;
        if (t.label == TriangleLabel.FACE) {
            Point3D lightSource = scene.getLightSource().applying(transform);
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
        return new Triangle2D(t, stroke, fill);
    }

    void renderModel(Graphics2D g, Mode mode) {
        g.setTransform(centerAndScale);
        for (Triangle2D t : triangles) {
            t.paintPolygon(g);
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
