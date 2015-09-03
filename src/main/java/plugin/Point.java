package plugin;

/**
 * Created by darryl on 3-9-15.
 */
public class Point {
    private final int x;
    private final int z;
    private final String type;

    public Point(int x, int z, String type) {
        this.x = x;
        this.z = z;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getType() {
        return type;
    }
}
