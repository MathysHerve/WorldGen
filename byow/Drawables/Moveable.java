package byow.Drawables;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGen.Draw;
import byow.Utilities.Interval2D;

import java.util.HashSet;
import java.util.Set;

/**
 * A Moveable is a Drawable that is aware of it's container. It can move in the 4 cardinal directions North, East,
 * West, and South. It can only move on certain tiles, which are defined in TILES_TO_MOVE_ON.
 */
public class Moveable extends Drawable {

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    Set<TETile> TILES_TO_MOVE_ON = new HashSet<>();
    public int x, y;
    Container container;
    TETile[][] hiddenTiles;

    public Moveable(Container container, int x, int y, int width, int height) {
        super(width, height);

        if (x < 0 || y < 0) throw new IndexOutOfBoundsException();
        if (x >= container.width || y >= container.height) throw new IndexOutOfBoundsException();

        this.size += Draw.fillRectangle(shape, Tileset.AVATAR, width, height, 0, 0);
        this.x = x; this.y = y;
        this.container = container;
        this.hiddenTiles = new TETile[width][height];

        this.place(x, y);
    }

    /**
     * If the direction leads to a valid position, move there.
     *
     * @param direction Direction to move in.
     */
    public void move(int direction) {
        if (!validMove(direction)) return;
        Draw.place(hiddenTiles, container.getShape(), x, y);

        System.out.println("moving " + direction);
        if (direction == NORTH) this.y += 1;
        if (direction == SOUTH) this.y -= 1;
        if (direction == EAST) this.x += 1;
        if (direction == WEST) this.x -= 1;


        place(this.x, this.y);
    }

    /**
     * Place the Playable Character at X,Y
     *
     * @param x X Coordinate.
     * @param y Y Coordinate.
     */
    public void place(int x, int y) {
        hiddenTiles = Draw.select(container.getShape(), new Interval2D(x, x + width, y, y + height));
        Draw.place(this.shape, container.getShape(), x, y);

    }


    /**
     * Will check if the (x,y) coordinate is a valid place for a Playable Character.
     *
     * @param x X Coordinate.
     * @param y Y Coordinate.
     * @return Boolean if the XY coordinate is within the board and on a
     * valid tile.
     */
    private boolean validXYforMove(int x, int y) {
        TETile[][] destination = container.getShape();
        if (!(x < container.width && x > 0)) return false;
        if (!(y < container.height && y > 0)) return false;
        return TILES_TO_MOVE_ON.contains(destination[x][y]);
    }

    /**
     * Checks: if the playable character moves in a certain direction, then the player will be on a valid coordinate.
     * @param direction N,S,E,W
     * @return Boolean if the direction of travel will result in a valid move.
     */
    private boolean validMove(int direction) {
        if (direction == NORTH) return validXYforMove(this.x, this.y + 1);
        if (direction == SOUTH) return validXYforMove(this.x, this.y - 1);
        if (direction == EAST) return validXYforMove(this.x + 1, this.y);
        if (direction == WEST) return validXYforMove(this.x - 1, this.y);
        return false;
    }



}
