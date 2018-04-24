package Graphics;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DragObject {

    public int button;
    public Point start;

    public DragObject(MouseEvent event) {
        button = event.getButton();
        start = event.getPoint();
    }

}
