import Graphics.Model;

import java.awt.event.*;

public class Controller implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {

    View view;

    Controller(View view, Model model) {
        this.view = view;
        view.setModel(model;
        view.addComponentListener(this);
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
        view.addMouseWheelListener(this);
    }

    //==================================================================================================================

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        refreshPolygons();
        centerAndScaleBasedOn(modelBounds, getBounds());
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
        refreshPolygons();
        centerAndScale = centerAndScaleBasedOn(modelBounds, getBounds());
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
        refreshPolygons();
        centerAndScale = centerAndScaleBasedOn(modelBounds, getBounds());
    }
    public void componentHidden(ComponentEvent componentEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        drag = new DragObject(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        drag = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        System.out.println(String.format("Mouse event button=%d", drag.button));
        double deltaX = mouseEvent.getX() - drag.start.x;
        double deltaY = mouseEvent.getY() - drag.start.y;
        drag.start = mouseEvent.getPoint();
        if (drag.button == MouseEvent.BUTTON1) {
            double radX = deltaX / getWidth();
            double radY = deltaY / getWidth();
            transform = transform.rotatedBy(radX, radY);
            refreshPolygons();
            repaint();
        } else if (drag.button == MouseEvent.BUTTON2) {
            double scale = centerAndScale.getScaleX();
            centerAndScale.translate(deltaX/scale, -deltaY/scale);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    //==================================================================================================================

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        double factor = 1.0 - (double)mouseWheelEvent.getUnitsToScroll() / 100.0;
        centerAndScale.scale(factor, factor);
        repaint();
    }

}
