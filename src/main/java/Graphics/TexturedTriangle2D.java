package Graphics;

import java.awt.*;

public class TexturedTriangle2D extends Triangle2D {

    Color c1, c2, c3;

    private TexturedTriangle2D(Point2D p1, Point2D p2, Point2D p3, double z, Color fill, Color stroke) {
        super(p1,p2,p3,z, fill, stroke);
    }

    public static TexturedTriangle2D MakeFrom(Triangle3D t) {
        int red = (t.c1.getRed() + t.c2.getRed() + t.c3.getRed()) / 3;
        int green = (t.c1.getGreen() + t.c2.getGreen() + t.c3.getGreen()) / 3;
        int blue = (t.c1.getGreen() + t.c2.getGreen() + t.c3.getGreen()) / 3;
        Color average = new Color(red, green, blue);
        TexturedTriangle2D triangle2D = new TexturedTriangle2D(t.get2DPoint1(),
                t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), average, null);
        triangle2D.c1 = t.c1;
        triangle2D.c2 = t.c2;
        triangle2D.c3 = t.c3;
        return triangle2D;
    }

    public static Triangle2D FlatMode(Triangle3D t, Point3D lightSource) {
        Color fill;
        Color stroke;
        if (t.label == TriangleLabel.FACE) {
            Vector3D directionToLightSource = Vector3D.vectorFromTo(t.getCentroid(), lightSource).normalized();
            Vector3D normal = t.normal;
            double dotProduct = Vector3D.dotProduct(directionToLightSource, t.normal);
            int greyscale = (int)Math.round(dotProduct * 255);
            if (greyscale < 0) {
                greyscale = 0;
            }
            fill = new Color(greyscale, greyscale, greyscale);
            stroke = null;
        } else {
            fill = Colors.lemon;
            stroke = Colors.tangerine;
        }
        return new Triangle2D(t.get2DPoint1(), t.get2DPoint2(), t.get2DPoint3(), t.getCentroidZ(), fill, stroke);
    }

    public void drawInto(MyContext context) {
        Rectangle intersect = context.bounds.intersection(bounds);
        int maxX = context.getWidth();
        int maxY = context.getHeight();
        if (isFrontSide) {
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
    }

}
