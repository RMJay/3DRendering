package Graphics;

public class Point3D {
    public int id; //equal to line number in data file
    public final double x, y, z;

    public Point3D(int id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("Point{id:%d, x:%g, y:%g, z:%g}", id, x, y, z);
    }
}
