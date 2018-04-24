package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Triangle2D {

    private final boolean isFrontSide;
    private final Point2D[] points;
    private final Line2DNormalForm[] lines;
    private final double z;
    private final int strokeRGB;
    private final int fillRGB;
    private final Rectangle bounds;

    public Triangle2D(Point2D p1, Point2D p2, Point2D p3, double z, Color fill, Color stroke) {
        points = new Point2D[]{ p1, p2, p3 };
        Line2DNormalForm[] lines = new Line2DNormalForm[3];
        lines[0] = Line2DNormalForm.lineThrough(p1, p2);
        lines[1] = Line2DNormalForm.lineThrough(p2, p3);
        lines[2] = Line2DNormalForm.lineThrough(p3, p1);
        this.lines = lines;
        this.z = z;
        isFrontSide = isAntiClockwise(points);
        if (fill != null) {
//            if (isFrontSide) {
                fillRGB = fill.getRGB();
//            } else {
//                fillRGB = Colors.grape.getRGB();
//            }
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

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point2D p : points) {
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
        bounds = new Rectangle((int)Math.floor(minX), (int)Math.floor(minY),
                (int)Math.ceil(maxX - minX), (int)Math.ceil(maxY - minY));
    }

//    public static final Comparator<Triangle2D> ZComparator = new Comparator<Triangle2D>() {
//        @Override
//        public int compare(Triangle2D t1, Triangle2D t2) {
//            if (t1 == null || t2 == null) {
//                System.out.println("why is there a null in here?");
//                return 0;
//            }
//            double delta = t1.z - t2.z;
//            if (delta > 0) {
//                return -1;
//            } else if (delta < 0) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//    };

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

    public void drawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();

        if (fillRGB < 0 || strokeRGB < 0) {
            for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
                for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                    if (pixelIsWithinTriangle(x, y)) {
                        if (z < context.zBuffer.getBufferedZ(x, y)) {
                            context.pixels.setRGB(x, y, fillRGB);
                            context.zBuffer.setZ(x, y, z);
                        }
                    }
                }
            }
        }
    }

    boolean pixelIsWithinTriangle(int x, int y) {
        double[] homogenousPoint = new double[] { x, y, 1.0 };
        if (isFrontSide) {
            return dotProduct(homogenousPoint, lines[0].homos) > 0 &&
                    dotProduct(homogenousPoint, lines[1].homos) > 0 &&
                    dotProduct(homogenousPoint, lines[2].homos) > 0;
        } else {
            return dotProduct(homogenousPoint, lines[0].homos) < 0 &&
                    dotProduct(homogenousPoint, lines[1].homos) < 0 &&
                    dotProduct(homogenousPoint, lines[2].homos) < 0;
        }

    }

    private static double dotProduct(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }

    private static boolean isAntiClockwise(Point2D[] points) {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += (points[(i+1)%3].x - points[i].x)*(points[(i+1)%3].y + points[i].y);
        }
        return sum < 0;
    }
}
