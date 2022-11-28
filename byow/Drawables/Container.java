package byow.Drawables;

import byow.TileEngine.Tileset;
import byow.Utilities.Interval2D;
import byow.Utilities.Map2D;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A Container is a Drawable Object that can contain other Drawable objects. It allows the user
 * to keep track of all other Drawables in the Container. This allows the user to access information about the other
 * objects in the Container. For example, the user can place objects with respect to other objects in the
 * container, allowing us to prevent overlapping elements.
 */
public abstract class Container extends Drawable{

    public Map2D<Drawable> contained = new Map2D<>();
    public Set<Point> takenPoints = new HashSet<>();

    public Container(int width, int height) {
        super(width, height);
    }

    public void empty() {
        fillNothing();
    }

    /**
     * Returns the list of all possible points where one can place the Drawable d, without intersecting
     * another Object in this container.
     *
     * @param d Drawable to check.
     * @return A list of points where the Drawable d can be placed.
     */
    public LinkedList<Point> availableSpots(Drawable d) {
        Set<Point> badSpots = new HashSet<>();
        Set<Point> availableSpots = new HashSet<>();

        for (Point takenPoint : takenPoints) {
            for (int x = 0; x < d.width + 1; x++) {
                for (int y = 0; y < d.height + 1; y++) {
                    badSpots.add(new Point(takenPoint.x - x, takenPoint.y - y));
                }
            }
        }

        for (int x = 0; x < this.width - d.width + 1; x++) {
            for (int y = 0; y < this.height - d.height + 1; y++) {
                if (!badSpots.contains(new Point(x,y))) {
                    availableSpots.add(new Point(x, y));
                }

            }
        }
        return new LinkedList<Point>(availableSpots);
    }

    /**
     * Places a drawable into the container at (x,y).
     *
     * @param d Drawable to put.
     * @param x X Coordinate.
     * @param y Y Coordinate.
     */
    public void put(Drawable d, int x, int y) {
        contained.put(new Interval2D(x,x + d.width - 1, y, y + d.height - 1), d);

        for (int w = 0; w < d.width; w++) {
            for (int h = 0; h < d.height; h++) {
                if (this.shape[x + w][y + h] != Tileset.NOTHING && d.shape[w][h] == Tileset.NOTHING) {
                    size--;
                    takenPoints.remove(new Point(x + w, y + h));
                } else if (this.shape[x + w][y + h] == Tileset.NOTHING && d.shape[w][h] != Tileset.NOTHING){
                    size++;
                    takenPoints.add(new Point(x + w, y + h));
                }
                this.shape[x + w][y + h] = d.shape[w][h];
            }
        }
    }
}
