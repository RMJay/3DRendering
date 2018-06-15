package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Triangle2D {

    enum Mode { POLYGON, LIGHTSOURCE, FLAT, PHONG, TEXTURE }
    enum PixelType { EDGE, INSIDE, OUTSIDE, BACK }

    private final Mode mode;
    private final boolean isFrontSide;
    private final Point2D[] points;
    private final Line2DNormalForm[] lines;
    private final Vector3D[] normals;
    private final double z;
    private final Color[] colors;
    private final Rectangle bounds;
    private final double brightness;
    private final Vector3D directionToLightSource;
    private final Vector2D v0, v1; //used in the barycentric interpolation algorithm
    private final double d00, d01, d11, invDenom;

    protected Triangle2D(Point2D p1, Point2D p2, Point2D p3, double z, Vector3D averageNormal, Vector3D n1, Vector3D n2, Vector3D n3,
                          Color c1, Color c2, Color c3, Vector3D directionToLightSource, Mode mode) {
        this.mode = mode;
        points = new Point2D[]{ p1, p2, p3 };
        Line2DNormalForm[] lines = new Line2DNormalForm[3];
        lines[0] = Line2DNormalForm.lineThrough(p1, p2);
        lines[1] = Line2DNormalForm.lineThrough(p2, p3);
        lines[2] = Line2DNormalForm.lineThrough(p3, p1);
        this.lines = lines;
        this.z = z;
        isFrontSide = isClockwise(points);
        colors = new Color[] { c1, c2, c3};
        this.directionToLightSource = directionToLightSource;
        double brightness = 1.0;
        if (directionToLightSource != null) {
            double dotProduct = Vector3D.dotProduct(directionToLightSource, averageNormal);
            brightness = dotProduct;
            if (brightness < 0) {
                brightness = 0;
            }
        }
        this.brightness = brightness;

        Vector3D[] normals = new Vector3D[3];
        normals[0] = n1;
        normals[1] = n2;
        normals[2] = n3;
        this.normals = normals;

        v0 = Vector2D.Subtract(points[1], points[0]);
        v1 = Vector2D.Subtract(points[2], points[0]);
        d00 = Vector2D.Dot(v0, v0);
        d01 = Vector2D.Dot(v0, v1);
        d11 = Vector2D.Dot(v1, v1);
        invDenom = 1.0 / (d00 * d11 - d01 * d01);

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
        Vector3D directionToLightSource = null;
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.getAverageNormal(),
                t.getN1(), t.getN2(), t.getN3(), c, c, c, directionToLightSource, Mode.POLYGON);
    }

    public static Triangle2D LightSource(Triangle3D t) {
        Color c = Color.WHITE;  //not used
        double brightness = 0.0; //not used
        Vector3D directionToLightSource = null;
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.getAverageNormal(),
                t.getN1(), t.getN2(), t.getN3(), c, c, c, directionToLightSource, Mode.LIGHTSOURCE);
    }

    public static Triangle2D FlatMode(Triangle3D t, Point3D lightSource) {
        Color c = Color.WHITE;  //not used
        Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.getAverageNormal(),
                t.getN1(), t.getN2(), t.getN3(), c, c, c, directionToLightSource, Mode.FLAT);
    }

    public static Triangle2D PhongMode(Triangle3D t, Point3D lightSource) {
        Color c = Color.WHITE;  //not used
        Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.getAverageNormal(),
                t.getN1(), t.getN2(), t.getN3(), c, c, c, directionToLightSource, Mode.PHONG);
    }

    public static Triangle2D

    TextureMode(Triangle3D t, Point3D lightSource) {
        Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), t.getAverageNormal(),
                t.getN1(), t.getN2(), t.getN3(), t.getC1(), t.getC2(), t.getC3(), directionToLightSource, Mode.TEXTURE);
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    public void drawInto(MyContext context) {
        if (isFrontSide) {
            switch (mode) {
                case POLYGON:
                    polygonDrawInto(context, Color.WHITE, Color.RED);
                    break;
                case LIGHTSOURCE:
                    polygonDrawInto(context, Colors.lemon, Colors.tangerine);
                    break;
                case FLAT:
                    flatDrawInto(context);
                    break;
                case TEXTURE:
                    textureDrawInto(context);
                    break;
                case PHONG:
                    phongDrawInto(context);
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

    private void polygonDrawInto(MyContext context, Color fill, Color stroke) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();

        int fillRGB = fill.getRGB();
        int strokeRGB = stroke.getRGB();

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
        int intRGB = grey;
        intRGB = (intRGB << 8) + grey;
        intRGB = (intRGB << 8) + grey;

        for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
            for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                PixelType pix = pixelType(x, y);
                if (pix == PixelType.INSIDE || pix == PixelType.EDGE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        context.pixels.setRGB(x, y, intRGB);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    private void phongDrawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();

        for (int x = intersect.x; x <= (intersect.x + intersect.width) && x < maxX; x++) {
            for (int y = intersect.y; y <= (intersect.y + intersect.height) && y < maxY; y++) {
                PixelType pix = pixelType(x, y);
                if (pix == PixelType.INSIDE || pix == PixelType.EDGE) {
                    if (z < context.zBuffer.getBufferedZ(x, y)) {
                        Point2D p = new Point2D(x, y);
                        double brightness = phongBrightness(p);
                        double ambient = 0.15;
                        brightness = ambient + brightness * 0.85;
                        int grey = (int)(255 * brightness);
                        int rgb = grey;
                        rgb = (rgb << 8) + grey;
                        rgb = (rgb << 8) + grey;
                        context.pixels.setRGB(x, y, rgb);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    double phongBrightness(Point2D p) {
        double[] bary = barycentricCoords(p);
        double dx = bary[0] * normals[0].dx + bary[1] * normals[1].dx + bary[2] * normals[2].dx;
        double dy = bary[0] * normals[0].dy + bary[1] * normals[1].dy + bary[2] * normals[2].dy;
        double dz = bary[0] * normals[0].dz + bary[1] * normals[1].dz + bary[2] * normals[2].dz;
        Vector3D normal = new Vector3D(dx, dy, dz).normalized();
        double brightness = Vector3D.dotProduct(directionToLightSource, normal);
        if (brightness < 0) {
            brightness = 0;
        }
        return brightness;
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
                        Point2D p = new Point2D(x, y);
                        double brightness = phongBrightness(p);
                        double ambient = 0.15;
                        brightness = ambient + brightness * 0.85;
                        int rgb = interpolateColor(p);
                        int red = (int)(((rgb >> 16) & 0xFF) * brightness);
                        int green = (int)(((rgb >> 8) & 0xFF) * brightness);
                        int blue = (int)((rgb & 0xFF) * brightness);
                        rgb = red;
                        rgb = (rgb << 8) + green;
                        rgb = (rgb << 8) + blue;
                        context.pixels.setRGB(x, y, rgb);
                        context.zBuffer.setZ(x, y, z);
                    }
                }
            }
        }
    }

    int interpolateColor(Point2D p) {
        double[] bary = barycentricCoords(p);
        int red = (int)(bary[0] * colors[0].getRed() + bary[1] * colors[1].getRed() + bary[2] * colors[2].getRed());
        int green = (int)(bary[0] * colors[0].getGreen() + bary[1] * colors[1].getGreen() + bary[2] * colors[2].getGreen());
        int blue = (int)(bary[0] * colors[0].getBlue() + bary[1] * colors[1].getBlue() + bary[2] * colors[2].getBlue());
        int result = red;
        result = (result << 8) + green;
        result = (result << 8) + blue;
        return result;
    }

    double[] barycentricCoords(Point2D p) {
        Vector2D v2 = Vector2D.Subtract(p, points[0]);
        double d20 = Vector2D.Dot(v2, v0);
        double d21 = Vector2D.Dot(v2, v1);
        double[] bary = new double[3];
        bary[1] = (d11 * d20 - d01 * d21) * invDenom;
        bary[2] = (d00 * d21 - d01 * d20) * invDenom;
        bary[0] = 1.0d - bary[1] - bary[2];
        return bary;
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
