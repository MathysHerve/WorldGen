package byow.WorldGen;

import byow.Helpers;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utilities.Interval2D;


import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Map.entry;


/**
 * Class contains all methods to alter 2D arrays of TETiles. All methods that draw will return an integer value
 * that describes how the 2D array was altered.
 * <p>
 *     I will define an empty tile as a Tileset.NOTHING or null.
 *
 * <p>
 *     For example, returning 5 means that the method turned 5 empty tiles into nonempty tiles.
 * </p>
 * <p>
 *     Returning -5 means that the method turned 5 nonempty tiles into empty tiles.
 * </p>
 * <p>
 *     Anything else can be a combination of erasing and adding tiles.
 * </p>
 */
public class Draw {

    public static final Map<TETile, Integer> TILE_TO_DARKEN = Map.ofEntries(entry(Tileset.FLOWER, 0), entry(Tileset.LOCKED_DOOR, 1),  entry(Tileset.GRASS, 2), entry(Tileset.TREE, 3));

    /**
     * Draws a TETile[][] onto another TETile[][] at coordinates (x,y).
     *
     * @param b TETile[][] to draw on.
     * @param shape Another TETile[][] that should be drawn onto board b. Tileset.NOTHING/null will be ignored.
     * @param x X value on the board to draw the shape.
     * @param y Y value on the board to draw the shape.
     */
    public static void drawShape(TETile[][] b, TETile[][] shape, int x, int y) {
        int width = shape.length; int height = shape[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (validPoint(b, x + i, y + j)) b[x + i][y + j] = shape[i][j];
            }
        }

    }

    /**
     * In some cases, we might want to select a very specific shape from a board. Below is an example of selecting a
     * plus shape from a board of random tiles. Where * is any non null tile, and ABC.. are TETiles.
     *<pre>
     * From:                   Selection:      (x,y):     Returns:
     * [ A, B, C, D, E]        [ _, *, _]      (1,1)      [ _, C, _]
     * [ F, G, H, I, J]        [ *, *, *]                 [ G, H, I]
     * [ K, L, M, N, O]        [ _, *, _]                 [ _, M, _]
     * [ P, Q, R, S, T]
     *</pre>
     * Additionally, We use the Draw.TILE_TO_DARKEN that maps a certain TETile to an Integer. This allows us
     * to fade out our selection. For example, If our selection contains Tileset.GRASS, then the tiles that
     * are selected by Tileset.GRASS
     *
     * @param from The 2D array to make the selection from.
     * @param shape a 2D array that will serve as the shape of the selection
     * @param x X value on the from board to start selecting
     * @param y Y value on the from board to start selection
     * @return the selection is a 2D array of same dimensions as the selection array.
     */
    public static TETile[][] select(TETile[][] from, TETile[][] shape, int x, int y) {
        int width = shape.length; int height = shape[0].length;
        TETile[][] result = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!validPoint(from, x + i, y + j)) result[i][j] = Tileset.NOTHING;
                else if (shape[i][j] != Tileset.NOTHING && shape[i][j] != null) {
                    if (TILE_TO_DARKEN.containsKey(shape[i][j])) result[i][j] = TETile.darkerTile(from[x + i][y + j], TILE_TO_DARKEN.get(shape[i][j]));
                    else result[i][j] = from[x + i][y + j];
                }
                else result[i][j] = Tileset.NOTHING;
            }

        }
        return result;
    }

    public static TETile[][] selectIfWithin(TETile[][] from, TETile[][] shape, int x, int y, Interval2D bounds) {
        int width = shape.length; int height = shape[0].length;
        TETile[][] result = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(!bounds.contains(x + i, y + j)) result[i][j] = Tileset.NOTHING;
                else if (!validPoint(from, x + i, y + j)) result[i][j] = Tileset.NOTHING;
                else if (shape[i][j] != Tileset.NOTHING && shape[i][j] != null) {
                    if (TILE_TO_DARKEN.containsKey(shape[i][j])){
                        result[i][j] = TETile.darkerTile(from[x + i][y + j], TILE_TO_DARKEN.get(shape[i][j]));
                    }
                    else result[i][j] = from[x + i][y + j];
                }
                else result[i][j] = Tileset.NOTHING;
            }

        }
        return result;
    }

    public static TETile[][] select(TETile[][] from, Interval2D selection) {
        int minx, miny, width, height;
        minx = (int) selection.x.min(); miny = (int) selection.y.min();;
        width = (int) selection.x.length(); height = (int) selection.y.length();

        TETile[][] result = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = from[minx + i][miny + j];
            }
        }
        return result;
    }

    /**
     * Places a 2D array onto another 2D array at the specified coordinates.
     *
     * @param shape The 2D array of tiles to place
     * @param destination The 2D array of tiles to place the shape
     * @param x X value to place the shape
     * @param y Y value to place the shape
     * @return The number of tiles changed.
     */
    public static int place(TETile[][] shape, TETile[][] destination, int x, int y) {
        int count = 0;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                count += drawPoint(destination, shape[i][j], x + i, y + j);
            }
        }
        return count;
    }

    /**
     * Places a hallway on the path. A hallway is a path of floor tiles surrounded by walls.
     *
     * @param b Board to draw on
     * @param path List of points to draw the hallway on.
     * @return Number of tiles changed
     */
    public static int drawHallway(TETile[][] b, List<Point> path) {
        int count = 0;
        for (Point p : path) {
            count += drawHallway(b, p.x, p.y);
        }
        return count;
    }

    /**
     * Draws a floor surrounded by walls at x and y. A wall is only placed on null/nothing tiles.
     *
     * @param b 2D Array to draw on
     * @param x X value to draw on
     * @param y Y value to draw on
     * @return Number of tiles changed.
     */
    public static int drawHallway(TETile[][] b, int x, int y) {
        int count = 0;
        drawPoint(b, Tileset.FLOOR, x, y);
        Set<TETile> replaceables = new HashSet<>();
        replaceables.add(Tileset.NOTHING);
        replaceables.add(null);


        count += drawRectangleIf(b, replaceables, Tileset.WALL, 3, 3, x - 1, y - 1);
        return count;
    }


    /**
     * Checks if a point (x,y) is within the bounds of a 2D array.
     *
     * @param b 2D array
     * @param x X coordinate to check
     * @param y Y coordinate to check
     * @return boolean if the point (x,y) is in the 2D array.
     */
    public static boolean validPoint(TETile[][] b, int x, int y) {
        if (x < 0 || x >= b.length) return false;
        if (y < 0 || y >= b[0].length) return false;
        return true;
    }

    /**
     * Fills a 2D array with Tileset.Nothing.
     *
     * @param b 2D Array to empty
     * @return Total number of tiles changed.
     */
    public static int fillNothing(TETile[][] b) {
        int count = 0;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                count += drawPoint(b, Tileset.NOTHING, i, j);

            }
        }
        return count;
    }


    /**
     *<pre>
     *         Draws a room: 4 walls filled with floor
     *
     *     ##################  _
     *     #................#  |
     *     #................#  |   Length = Y coordinate / Height
     *     ##################  _
     *     |-----------------| Width = X coordinate
     *</pre>
     * @param b 2D array to draw on
     * @param width Width of the room.
     * @param height Height of the room.
     * @param x Bottom left X coordinate to draw on.
     * @param y Bottom right Y coordinate to draw on.
     * @return Total number of tiles changed.
     */
    public static int drawRoomDown(TETile[][] b, int width, int height, int x, int y) {
        int count = 0;
        int halfWidth = width/2;
        int halfHeight = height/2;
        // Draw Walls
        count += drawRectangle(b, Tileset.WALL, width, height, x, y);

        // Draw Floor

        count += fillRectangle(b, Tileset.FLOOR, width - 2, height - 2, x + 1, y + 1);
        count += drawPoint(b, Tileset.FLOOR, x + halfWidth, y);

        return count;
    }

    public static int drawRoom(TETile[][] b, int width, int height, int x, int y) {
        int count = 0;
        int halfWidth = width/2;
        int halfHeight = height/2;
        // Draw Walls
        count += drawRectangle(b, Tileset.WALL, width, height, x, y);

        // Draw Floor

        count += fillRectangle(b, Tileset.FLOOR, width - 2, height - 2, x + 1, y + 1);
        count += drawPoint(b, Tileset.FLOOR, x + halfWidth, y);
        count += drawPoint(b, Tileset.FLOOR, x + halfWidth, y + height - 1);

        return count;
    }

    public static int drawOpenRoom(TETile[][] b, int width, int height, int x, int y) {
        int count = 0;
        int halfWidth = width/2;
        int halfHeight = height/2;
        // Draw Walls
        count += drawRectangle(b, Tileset.WALL, width, height, x, y);

        // Draw Floor

        count += fillRectangle(b, Tileset.FLOOR, width - 2, height - 2, x + 1, y + 1);
        count += drawVLine(b, Tileset.FLOOR, height,x + halfWidth, 0);
        count += drawHLine(b, Tileset.FLOOR, width, 0, y + halfHeight);

        return count;
    }

    /*
    [A, B, C, i ]          [i, ii, iii]
    [D, E, F, ii ]   --->  [C,  F,  I]
    [G, H, I, iii]         [B,  E,  H]
                           [A,  D,  G]
     */
    public static TETile[][] rotateLeft(TETile[][] shape) {
        TETile[][] newShape = new TETile[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = columnToRow(shape, shape.length - 1 - i);
        }
        return newShape;
    }

    public static TETile[] columnToRow(TETile[][] shape, int column) {
        TETile[] result = new TETile[shape.length];
        for (int i = 0; i < shape.length; i++) {
            result[i] = shape[i][column];
        }
        return result;
    }



    /**
     * <pre>    Draws a filled rectangle:
     *
     *     ****************  _
     *     ****************  |  Length
     *     ****************  -
     *     |--------------| Width</pre>
     * @param b 2D array to draw on.
     * @param t Tile to draw the rectangle with.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     * @param x X coordinate.
     * @param y Y coordinate
     * @return Total number of tiles changed.
     */
    public static int fillRectangle(TETile[][] b, TETile t, int width, int height, int x, int y) {
        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    count += drawPoint(b, t, x + i, y + j);
                } catch (Exception ignored) {
                }
            }
        }
        return count;
    }


    /**<pre>
     *       Draws a hollow rectangle
     *     ****************
     *     *              *
     *     ****************
     *
     * </pre>
     * @param b 2D array to draw on.
     * @param t Tile to draw the rectangle with.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     * @param x X coordinate.
     * @param y Y coordinate
     * @return Total number of tiles changed.
     */
    public static int drawRectangle(TETile[][] b, TETile t, int width, int height, int x, int y) {
        int count = 0;
        // Left Wall
        count += drawVLine(b, t, height, x, y);
        // Right Wall. If (0, 0) then we want this line at (9, 0) AKA x + length - 1
        count += drawVLine(b, t, height, x + width - 1, y);
        //Bottom Wall
        count += drawHLine(b, t, width, x, y);
        //Top Wall.
        count += drawHLine(b, t, width, x, y + height - 1);
        return count;
    }

    public static int drawRectangleIf(TETile[][] b, Set<TETile> from, TETile to, int width, int height, int x, int y) {
        int count = 0;
        // Left Wall
        count += drawVLineIf(b, from, to, height, x, y);
        // Right Wall. If (0, 0) then we want this line at (9, 0) AKA x + length - 1
        count += drawVLineIf(b, from, to, height, x + width - 1, y);
        //Bottom Wall
        count += drawHLineIf(b, from, to, width, x, y);
        //Top Wall.
        count += drawHLineIf(b, from, to, width, x, y + height - 1);
        return count;
    }


    /**
     * <pre>
     *         Draws a Vertical Line
     *
     *     *
     *     *
     *     *
     *     *
     *     *
     * </pre>
     * @param b 2D array to draw on.
     * @param t Tile to draw the rectangle with.
     * @param height Height of the rectangle.
     * @param x X coordinate.
     * @param y Y coordinate
     * @return Total number of tiles changed.
     */
    public static int drawVLine(TETile[][] b, TETile t, int height, int x, int y) {
        int count = 0;
        for (int i = 0; i < height; i++) {
            try {
                count += drawPoint(b, t, x, y + i);
            } catch (Exception ignored) {
            }

        }
        return count;
    }



    public static int drawVLineIf(TETile[][] b, Set<TETile> from, TETile to, int height, int x, int y) {
        int count = 0;
        for (int i = 0; i < height; i++) {
            try {
                count += drawPointIf(b, from, to, x, y + i);
            } catch (Exception ignored) {
            }

        }
        return count;
    }

    /**
     * <pre>
     *      Draws a Horizontal Line
     *
     *      *******************
     *</pre>
     * @param b 2D array to draw on.
     * @param t Tile to draw the rectangle with.
     * @param width Width of the rectangle.
     * @param x X coordinate.
     * @param y Y coordinate
     * @return Total number of tiles changed.
     */
    public static int drawHLine(TETile[][] b, TETile t, int width, int x, int y) {
        int count = 0;
        for (int i = 0; i < width; i++) {
            try {
                count += drawPoint(b, t, x + i, y);
            } catch (Exception ignored) {
            }

        }
        return count;
    }

    public static int drawHLineIf(TETile[][] b, Set<TETile> from, TETile to, int width, int x, int y) {
        int count = 0;
        for (int i = 0; i < width; i++) {
            try {
                count += drawPointIf(b, from, to, x + i, y);
            } catch (Exception ignored) {
            }

        }
        return count;
    }

    /**
     * Draws a point with tile t at (x,y) on the 2D array b.
     *
     * @param b 2D array to draw on.
     * @param t Tile to use.
     * @param x X Coordinate
     * @param y Y Coordinate
     * @return 1 if the tile changed from nothing, 0 if not, -1 if the tile was set to Nothing.
     */
    public static int drawPoint(TETile[][] b, TETile t, int x, int y) {
        int count = 0;
        TETile curr_tile = b[x][y];
        if (!validPoint(b, x, y)) {
            System.out.println(b.length + " " + b[0].length);
            System.out.println(x + " " + y);
            return 0;
        }

        if ((curr_tile) == Tileset.NOTHING && t != Tileset.NOTHING) {
            count += 1;
        } else if (!(curr_tile == Tileset.NOTHING || curr_tile == null) && t == Tileset.NOTHING) {
            count -= 1;
        }

        b[x][y] = t;

        return count;
    }


    public static int drawPointIf(TETile[][] b, Set<TETile> from, TETile to, int x, int y) {
        int count = 0;
        if (from.contains(b[x][y])) {
            count += Draw.drawPoint(b, to, x, y);
        }
        return count;
    }



}
