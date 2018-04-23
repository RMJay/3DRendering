package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Triangle2D {

    private final Point[] points;
    private final double z;
    private final int strokeRGB;
    private final int fillRGB;
    private final Rectangle bounds;

//    public Triangle2D(Triangle3D t, Color stroke, Color fill) {
//        int numPoints = 3;
//        int[] xPoints = { t.v1.intX(), t.v2.intX(), t.v3.intX() };
//        int[] yPoints = { t.v1.intY(), t.v2.intY(), t.v3.intY() };
//        polygon = new Polygon(xPoints, yPoints, numPoints);
//        z = t.getCentroidZ();
//        this.stroke = stroke;
//        this.fill = fill;
//    }

    public Triangle2D(Point p1, Point p2, Point p3, double z, Color fill, Color stroke) {
        this.points = new Point[]{ p1, p2, p3 };
        this.z = z;
        if (fill != null) {
            fillRGB = fill.getRGB();
        } else {
            //int RGB values are negative?
            fillRGB = 1;
        }
        if (stroke != null) {
            strokeRGB = stroke.getRGB();
        } else {
            //int RGB values are negative?
            strokeRGB = 1;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : points) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
        }
        bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public static final Comparator<Triangle2D> ZComparator = new Comparator<Triangle2D>() {
        @Override
        public int compare(Triangle2D t1, Triangle2D t2) {
            if (t1 == null || t2 == null) {
                System.out.println("why is there a null in here?");
                return 0;
            }
            double delta = t1.z - t2.z;
            if (delta > 0) {
                return -1;
            } else if (delta < 0) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

//    public void paintPolygon(Graphics2D g) {
//        g.setColor(fill);
//        g.fillPolygon(polygon);
//        if (stroke != null) {
//            g.setColor(stroke);
//            g.drawPolygon(polygon);
//        }
//    }

    public void drawInto(BufferedImage pixels, ZBuffer zBuffer) {
        System.out.println(String.format("fillRGB=%d", fillRGB));
        if (fillRGB < 0) {
            System.out.println("Here");
            for (int x = bounds.x; x < (bounds.x + bounds.width); x++) {
                for (int y = bounds.y; y < (bounds.y + bounds.height); y++) {
                    if (z < zBuffer.getBufferedZ(x, y)) {
                        pixels.setRGB(x, y, fillRGB);
                        zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }
}
