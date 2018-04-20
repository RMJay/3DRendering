import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import Graphics.*;

public class View extends JPanel {

    final Model modelInModelCoords;
    Polygon[] polygons = null;
    AffineTransform3D viewTransform = AffineTransform3D.identity();

    public View(Model model) {
        this.modelInModelCoords = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintBackground(g2d);
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

    refreshPolygons() {

    }

}
