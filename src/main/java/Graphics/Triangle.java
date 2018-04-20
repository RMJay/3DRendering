package Graphics;

public class Triangle {
    final Point3D v1, v2, v3;

    public Triangle(Point3D v1, Point3D v2, Point3D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public String getId(){  //the line numbers of the three points "#_#_#"
        return String.format("%d_%d_%d", v1.id, v2.id, v3.id);
    };

    @Override
    public String toString() {
        return String.format("Triangle{id: %s, %s, %s, %s}", getId(), v1.toString(), v2.toString(), v3.toString());
    }
}
