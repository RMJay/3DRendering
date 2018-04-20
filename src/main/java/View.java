import javax.swing.*;
import java.awt.*;
import Graphics.*;

public class View extends JPanel {

    FaceGraphic faceGraphic;

    public View(FaceGraphic faceGraphic) {
        this.faceGraphic = faceGraphic;
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

}
