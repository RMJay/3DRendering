import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import Graphics.*;

public class RenderView extends JPanel {

    enum Mode { POLYGONS, TEXTURE }

    private Mode mode = Mode.POLYGONS;
    private Model model = null;
    private Polygon[] polygons = null;
    private Rectangle modelBounds = null;
    private AffineTransform3D transform = AffineTransform3D.identity().rotatedBy(Math.PI, 0.0);
    private AffineTransform centerAndScale = null;

    public void setModel(Model model) {
        this.model = model;
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

    public void refreshPolygons() {
        Model modelInSceneCoords = model.applying(transform);

        Polygon[] polygons = new Polygon[modelInSceneCoords.triangles.length];
        for (int i = 0; i < modelInSceneCoords.triangles.length; i++) {
            polygons[i] = makePolygonFrom(modelInSceneCoords.triangles[i]);
        }
        this.polygons = polygons;
        Rectangle bounds = null;
        for (int i = 0; i < polygons.length; i++) {
            Rectangle b = polygons[i].getBounds();
            if (bounds == null) {
                bounds = b;
            } else {
                Rectangle.union(bounds, polygons[i].getBounds(), bounds);
            }
        }
        modelBounds = bounds;
    }

    Polygon makePolygonFrom(Triangle t) {
        int numPoints = 3;
        int[] xPoints = { t.v1.intX(), t.v2.intX(), t.v3.intX() };
        int[] yPoints = { t.v1.intY(), t.v2.intY(), t.v3.intY() };
        return new Polygon(xPoints, yPoints, numPoints);
    }

    void renderModel(Graphics2D g, Mode mode) {
        System.out.println("Render Mode: " + mode.toString());
        switch (mode) {
            case POLYGONS:
                renderModelPolygons(g);
                break;
            case TEXTURE:
                renderModelTexture(g);
                break;
        }
    }

    void renderModelPolygons(Graphics2D g) {
        if (polygons != null) {
            g.setTransform(centerAndScale);
            for (Polygon p : polygons) {
                g.setColor(Color.WHITE);
                g.fillPolygon(p);
                g.setColor(Color.RED);
                g.drawPolygon(p);
            }
        }
    }

    void renderModelTexture(Graphics2D g) {
        //not implemented
    }

    public void showBounds(Graphics2D g) {
        if (modelBounds != null) {
            g.setColor(Color.PINK);
            g.drawRect(modelBounds.x, modelBounds.y, modelBounds.width, modelBounds.height);
        }
    }

    void centerAndScale() {
        refreshPolygons();
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
        refreshPolygons();
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
