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

    public void drawZBuffer() {
        double minFiniteZ = Double.POSITIVE_INFINITY;
        double maxFiniteZ = Double.NEGATIVE_INFINITY;
        double raw;
        for (int x = 0; x < bounds.width; x++) {
            for (int y = 0; y < bounds.height; y++) {
                raw = zBuffer.getBufferedZ(x, y);
                if (raw != Double.POSITIVE_INFINITY) {
                    if (minFiniteZ == Double.POSITIVE_INFINITY) {
                        minFiniteZ = raw;
                    } else if (raw < minFiniteZ) {
                        minFiniteZ = raw;
                    }
                    if (maxFiniteZ == Double.NEGATIVE_INFINITY) {
                        maxFiniteZ = raw;
                    } else if (raw > maxFiniteZ) {
                        maxFiniteZ = raw;
                    }
                }
            }
        }
        double delta = maxFiniteZ - minFiniteZ;
        for (int x = 0; x < bounds.width; x++) {
            for (int y = 0; y < bounds.height; y++) {
                raw = zBuffer.getBufferedZ(x, y);
                if (raw != Double.POSITIVE_INFINITY) {
                    pixels.setRGB(x, y, getColorRGB(raw, maxFiniteZ, delta));
                }
            }
        }
    }

    int getColorRGB(double raw, double maxFiniteZ, double delta) {
        if (raw == Double.POSITIVE_INFINITY) {
            return 0;
        } else {
            int greyscale = (int)((maxFiniteZ - raw) / delta * 255);
            int rgb = greyscale;
            rgb = (rgb << 8) + greyscale;
            rgb = (rgb << 8) + greyscale;
            return rgb;
        }
    }
}
