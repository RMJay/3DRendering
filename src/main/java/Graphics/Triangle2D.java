package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Triangle2D {

    enum Mode { POLYGON, FLAT, TEXTURE }
    enum PixelType { EDGE, INSIDE, OUTSIDE, BACK }

    private final Mode mode;
    private final boolean isFrontSide;
    private final Point2D[] points;
    private final Line2DNormalForm[] lines;
    private final double z;
    private final Color c1, c2, c3;
    private final Rectangle bounds;
    private final double brightness;

    protected Triangle2D(Point2D p1, Point2D p2, Point2D p3, double z, Color c1, Color c2, Color c3, double brightness, Mode mode) {
        this.mode = mode;
        points = new Point2D[]{ p1, p2, p3 };
        Line2DNormalForm[] lines = new Line2DNormalForm[3];
        lines[0] = Line2DNormalForm.lineThrough(p1, p2);
        lines[1] = Line2DNormalForm.lineThrough(p2, p3);
        lines[2] = Line2DNormalForm.lineThrough(p3, p1);
        this.lines = lines;
        this.z = z;
        isFrontSide = isClockwise(points);
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.brightness = brightness;

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

    public static Triangle2D PolygonMode(Triangle3D t) {
        Color c = Color.WHITE;  //not used
        double brightness = 0.0; //not used
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), c, c, c, brightness, Mode.POLYGON);
    }

    public static Triangle2D FlatMode(Triangle3D t, Point3D lightSource) {
        Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
        Vector3D normal = t.normal;
        double dotProduct = Vector3D.dotProduct(directionToLightSource, t.normal);
        double brightness = dotProduct;
        if (brightness < 0) {
            brightness = 0;
        }
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.c1, t.c2, t.c3, brightness, Mode.FLAT);
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    public void drawInto(MyContext context) {
        if (isFrontSide) {
            switch (mode) {
                case POLYGON:
                    polygonDrawInto(context);
                    break;
                case FLAT:
                    flatDrawInto(context);
                    break;
                case TEXTURE:
                    flatDrawInto(context);
                    break;
            }
        }
    }

    private void polygonDrawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();

        int fillRGB = Color.WHITE.getRGB();
        int strokeRGB = Color.RED.getRGB();

        for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
            for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                PixelType pix = pixelType(x, y);
                if (pix == PixelType.EDGE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        context.pixels.setRGB(x, y, strokeRGB);
                        context.zBuffer.setZ(x, y, z);
                    }
                } else if (pix == PixelType.INSIDE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        context.pixels.setRGB(x, y, fillRGB);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    private void flatDrawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();
        int grey = (int)(brightness * 255);
        grey = (grey << 8) + grey;
        grey = (grey << 8) + grey;

        for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
            for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                PixelType pix = pixelType(x, y);
                if (pix == PixelType.INSIDE || pix == PixelType.EDGE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        context.pixels.setRGB(x, y, grey);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    private void textureDrawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();

        for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
            for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                PixelType pix = pixelType(x, y);
                if (pix == PixelType.INSIDE || pix == PixelType.EDGE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        context.pixels.setRGB(x, y, fillRGB);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    PixelType pixelType(int x, int y) {
        double[] homogenousPoint = new double[] { x, y, 1.0 };
        double dot1 = dotProduct(homogenousPoint, lines[0].homos);
        double dot2 = dotProduct(homogenousPoint, lines[1].homos);
        double dot3 = dotProduct(homogenousPoint, lines[2].homos);
            if (dot1 < 0.5 && dot2 < 0.5 && dot3 < 0.5) {
                if (dot1 > -0.5 || dot2 > -0.5 || dot3 > -0.5) {
                    return PixelType.EDGE;
                }
                return PixelType.INSIDE;
            }
            return PixelType.OUTSIDE;
    }

    private static double dotProduct(double[] a, double[] b) {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }

    private static boolean isClockwise(Point2D[] points) {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += (points[(i+1)%3].x - points[i].x)*(points[(i+1)%3].y + points[i].y);
        }
        return sum > 0;
    }
}
