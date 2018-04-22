import java.awt.*;
import java.awt.event.MouseEvent;

public class DragObject {

    int button;
    Point start;

    public DragObject(MouseEvent event) {
        button = event.getButton();
        start = event.getPoint();
    }

}
