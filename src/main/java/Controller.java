import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

import Graphics.*;

public class Controller extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, KeyListener,
                                                  MouseWheelListener, ActionListener {

    private JPanel topBar;
    private JComboBox<RenderView.Mode> modeSelector;
    private RenderView renderView;
    private DragObject drag = null;
    private final HashSet<Integer> keysPressed = new HashSet<Integer>();
    private int timerDelay = 100;
    private Timer arrowKeyTimer = new Timer(timerDelay, this); //timer delay is 100 milliseconds
    private long startTime;

    Controller(RenderView renderView, Face face) {
        setLayout(new BorderLayout());
        topBar = new JPanel();
        add(topBar, BorderLayout.PAGE_START);

        Label modePrompt = new Label("Render mode", Label.LEFT);
        topBar.add(modePrompt);
        topBar.setBackground(Colors.orchid);
        modeSelector = new JComboBox<>(RenderView.Mode.values());
        topBar.add(modeSelector);
        modeSelector.addActionListener(this);

        this.renderView = renderView;
        add(renderView, BorderLayout.CENTER);
        renderView.setFace(face);
        renderView.addComponentListener(this);
        renderView.addMouseListener(this);
        renderView.addMouseMotionListener(this);
        renderView.addMouseWheelListener(this);
        renderView.addKeyListener(this);
        renderView.requestFocus();
    }

    //==================================================================================================================

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        renderView.centerAndScale();
        renderView.refreshTriangles();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
        renderView.centerAndScale();
        renderView.refreshTriangles();
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
        renderView.centerAndScale();
        renderView.refreshTriangles();
    }

    public void componentHidden(ComponentEvent componentEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        renderView.requestFocus();
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
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (isArrowKey(keyCode)) {
            keysPressed.add(keyCode);
            arrowKeyTimer.start();
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (isArrowKey(keyCode)) {
            keysPressed.remove(keyEvent.getKeyCode());
            if (keysPressed.size() == 0) {
                if ((System.currentTimeMillis() - startTime) < timerDelay) {
                    moveLight(keyCode, 3000);
                }
                arrowKeyTimer.stop();
                startTime = -1;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == modeSelector) {
            RenderView.Mode selected = (RenderView.Mode)modeSelector.getSelectedItem();
            renderView.setMode(selected);
            renderView.centerAndScale();
            renderView.refreshTriangles();
            renderView.repaint();
        } else if (event.getSource() == arrowKeyTimer) {
            if (keysPressed.size() == 1) {
                int keyCode = keysPressed.iterator().next();
                moveLight(keyCode, 3000);
            }
        }
    }

    boolean isArrowKey(int keyCode) {
        return keyCode == KeyEvent.VK_LEFT ||
                keyCode == KeyEvent.VK_RIGHT ||
                keyCode == KeyEvent.VK_UP ||
                keyCode == KeyEvent.VK_DOWN;
    }

    void moveLight(int keyCode, double increment) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                renderView.moveLight(-increment, 0.0);
                break;
            case KeyEvent.VK_RIGHT:
                renderView.moveLight(increment, 0.0);
                break;
            case KeyEvent.VK_UP:
                renderView.moveLight(0.0, -increment);
                break;
            case KeyEvent.VK_DOWN:
                renderView.moveLight(0.0, increment);
                break;
        }
    }
}
