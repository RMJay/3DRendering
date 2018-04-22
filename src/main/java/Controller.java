import Graphics.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Graphics.*;

public class Controller extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, ActionListener {

    private JPanel topBar;
    private JComboBox<RenderView.Mode> modeSelector;
    private RenderView renderView;
    private DragObject drag = null;

    Controller(RenderView renderView, Model model) {
        setLayout(new BorderLayout());
        JPanel topBar = new JPanel();
        add(topBar, BorderLayout.PAGE_START);

        Label modePrompt = new Label("Render mode", Label.LEFT);
        topBar.add(modePrompt);
        topBar.setBackground(Colors.orchid);
        modeSelector = new JComboBox<>(RenderView.Mode.values());
        topBar.add(modeSelector);

        this.renderView = renderView;
        add(renderView, BorderLayout.CENTER);
        renderView.setModel(model);
        renderView.addComponentListener(this);
        renderView.addMouseListener(this);
        renderView.addMouseMotionListener(this);
        renderView.addMouseWheelListener(this);
    }

    //==================================================================================================================

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        renderView.centerAndScale();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
        renderView.centerAndScale();
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
        renderView.centerAndScale();
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
            double radX = deltaX / renderView.getWidth();
            double radY = deltaY / renderView.getWidth();
            renderView.rotateBy(radX, radY);
        } else if (drag.button == MouseEvent.BUTTON2) {
            renderView.scrollBy(deltaX, deltaY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        double factor = 1.0 - (double)mouseWheelEvent.getUnitsToScroll() / 100.0;
        renderView.zoomBy(factor);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
