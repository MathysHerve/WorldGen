package byow.WorldGen;

import byow.Core.Engine;
import byow.Drawables.Board;
import byow.Drawables.Room;
import byow.TileEngine.TERenderer;
import byow.Utilities.Interval2D;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Generate world is responsible for all Pseudorandom generation. It is used mainly to create room + hallway
 * configurations using a specific Random instance.
 *
 */
public class GenerateWorld {
    private static final int KEYBOARD = 0;
    private static final int RANDOM = 1;
    private static final int STRING = 2;

    private Random r;

    public GenerateWorld() {
        r = new Random();
    }

    public GenerateWorld(long seed) { r = new Random(seed); }

    public void setSeed(long seed) {
        this.r = new Random(seed);
    }


    /**
     * Builds a room of random dimension in a random spot on a Board.
     *
     * @param board               Board to draw on
     * @param availableDimensions All possible dimensions to choose from.
     * @return True if room was build successfully.
     */
    // Max is 10, min is 4
    public boolean buildRoom(Board board, List<Point> availableDimensions) {
        // Are there any rooms that we can even build?
        if (availableDimensions.size() == 0) {
            return false;
        }
        // If we can, lets take the dimensions and make a room.
        Point randDimension = availableDimensions.get(r.nextInt(availableDimensions.size()));
        Room room = new Room(randDimension.x, randDimension.y);
        // Are there any spots available for that room?
        List<Point> availablePoints = board.availableSpots(room);
        // Yes: Put the room.
        if (availablePoints.size() > 0) {
            Point randCoord = availablePoints.get(r.nextInt(availablePoints.size()));
            board.put(room, randCoord.x, randCoord.y);
            return true;
        }
        // No: The dimensions are not available anymore, try a different room.
        availableDimensions.remove(randDimension);
        return buildRoom(board, availableDimensions);

    }


    /**
     * Returns a random list of points that connects point 1 and point 2.
     *
     * @param p1 Point 1
     * @param p2 Point 2
     * @return A List of Points connecting p1 to p2.
     */
    public List<Point> randPath(Point p1, Point p2) {
        List<Point> result = new LinkedList<>();
        result.add(p1);
        int randSwitch = r.nextInt(2);
        // Point 1 == Point 2. Return a single element list with the point.
        if (p1.x == p2.x && p1.y == p2.y) return result;
        // If the points are on the same x, force the path to only go towards the Y direction.
        if (p1.x == p2.x) randSwitch = 1;
        // If the points are on the same y, force the path to only go towards the X direction.
        if (p1.y == p2.y) randSwitch = 0;
        switch (randSwitch) {
            case 0:
                if (p1.x < p2.x) {
                    result.addAll(randPath(new Point(p1.x + 1, p1.y), p2));
                    return result;
                } else {
                    result.addAll(randPath(new Point(p1.x - 1, p1.y), p2));
                    return result;
                }

            case 1:
                if (p1.y < p2.y) {
                    result.addAll(randPath(new Point(p1.x, p1.y + 1), p2));
                    return result;
                } else {
                    result.addAll(randPath(new Point(p1.x, p1.y - 1), p2));
                    return result;
                }

        }

        return result;
    }

    /**
     * Draws a pseudorandom configuration of rooms and hallways
     *
     * @param board  Board to generate on.
     * @param max    Upper bound for rooms
     * @param min    Lower bound for rooms.
     * @param amount Amount of rooms to spawn.
     */
    public void genRooms(Board board, int max, int min, int amount) {
        List<Point> availableDimensions = allPossibleDimensions(min, max);
        int roomsBuilt = 0;
        while (buildRoom(board, availableDimensions)) {
            roomsBuilt += 1;
            if (roomsBuilt >= amount) {
                break;
            }
        }
        // Linked Hash Set is necessary for pseudorandom. Regular Set's do not guarantee order.
        LinkedHashSet<Interval2D> keys = (LinkedHashSet<Interval2D>) board.contained.keySet();
        Set<Interval2D> marked = new HashSet<>();

        for (Interval2D key : keys) {
            marked.add(key);
            List<Interval2D> nearest = board.contained.slowNearest(key);
            nearest.removeAll(marked);
            if (nearest.size() >= 1) {
                for (int j = 0; j < 1; j++) {
                    Draw.drawHallway(board.getShape(), randPath(nearest.get(j).center(), key.center()));
                }
            }

        }

    }

    public Point randomCoord(int width, int height) {
        return new Point(r.nextInt(width), r.nextInt(height));
    }

    /**
     * Returns a list of all permutations of (width, height) where width and height are bounded by min and max.
     *
     * @param min Lower bound of width/height
     * @param max Upper bound of width/height
     * @return List of Permutations (Width, Height)
     */
    public List<Point> allPossibleDimensions(int min, int max) {
        List<Point> availableDimensions = new ArrayList<>();
        for (int width = min; width <= max; width++) {
            for (int height = min; height <= max; height++) {
                availableDimensions.add(new Point(width, height));
            }
        }
        return availableDimensions;
    }

    public static void main(String[] args) {

        Engine e = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);

        ter.renderFrame(e.interactWithInputString("N123"));


    }
}
