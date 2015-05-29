/**
 * Created by darryl on 25-5-15.
 */
public class Coords {
    private final int x;
    private final int z;
    private final int y;
    private final int type;

    public Coords(int x, int z) {
        this(x, z, -1);
    }

    public Coords(int x, int z, int y) {
        this(x, z, y, 0);
    }

    public Coords(int x, int z, int y, int type) {
        this.x = x;
        this.z = z;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "x=" + x +
                ", z=" + z +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}