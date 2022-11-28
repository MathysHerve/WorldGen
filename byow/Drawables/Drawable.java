package byow.Drawables;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import byow.WorldGen.Draw;


import java.awt.*;
import java.io.Serializable;

/**<p>
 * A Drawable is a 2D Array that contains all information about itself that might be useful for drawing
 * on a GUI.
 * <p>
 * The main parts to a drawable are its shape, dimension, and size.
 * The dimensions define how big the 2D array is by width and height.
 * <p>
 * I will define an empty tile as Tileset.NOTHING or null.
 * <p>
 * The shape of a Drawable is a 2D array that is of width and height, and contains TETiles
 * that show what it would look like when you draw it on a screen. The size is the number of non empty tiles in
 * the shape.
 * <p>
 *     For example, a drawable with width 3 and height 3 and size 5. It's 2D array is 3 x 3, but that does not mean
 *     it has to be a Square. Here is what a Plus would look like
 *
 *     Where * Indicates any TETile that is NOT Tileset.NOTHING or null.
 *     The _ Indicates an empty tile.
 *
 * <pre>
 *     [ _, *, _]
 *     [ *, *, *]
 *     [ _, *, _]
 * </pre>
 *
 *     A mix of empty tiles and not empty tiles allows us to draw any shape we can think of, while allowing us to
 *     combine them.
 */
public abstract class Drawable implements Serializable {
    int width;
    int height;
    Point center;
    int size = 0;
    TETile[][] shape;

    public Drawable(int width, int height) {
        this.width = width;
        this.height = height;
        this.center = new Point(width / 2, height / 2);
        this.shape = new TETile[width][height];
        fillNothing();
    }
    public TETile[][] getShape() {
        return this.shape;
    }
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    public void draw() {
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(shape);
    }

    void fillNothing() {
        this.size += Draw.fillNothing(shape);
    }


    public String toString() {
        return "Drawable: W: " + width + " H: " + height + " S: " + size;
    }
}
