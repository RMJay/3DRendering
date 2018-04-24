package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MyContext {

    public final Rectangle bounds;
    public final BufferedImage pixels;
    public final ZBuffer zBuffer;

    public MyContext(int width, int height) {
        bounds = new Rectangle(0,0, width, height);
        pixels = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        zBuffer = new ZBuffer(width, height);
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }
}
