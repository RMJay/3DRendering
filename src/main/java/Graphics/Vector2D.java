package Graphics;

public class Vector2D {

    public final double dx, dy;

    public Vector2D(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    static Vector2D Subtract(Point2D a, Point2D b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return new Vector2D(dx, dy);
    }

    static double Dot(Vector2D a, Vector2D b) {
        return a.dx * b.dx + a.dy * b.dy;
    }
}
