package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;


public class Tile {
    static final int width = 7;
    static final int num_sockets = 4;
    final TETile[][] shape;

    int[] sockets = new int[num_sockets];
    Map<Directions, Set<Tile>> validNeighborsCache = new HashMap<>();

    public Tile(TETile[][] _shape) {
        if (_shape.length != width || _shape[0].length != width) throw new IndexOutOfBoundsException();
        shape = _shape;

        int i = 0;
        for (Directions d : Directions.values()) {
            sockets[i] = stripToSocket(getStrip(d));
            i++;
        }
    }


    public void cacheValidNeighbors() {
        for (Directions direction : Directions.values()) {
            validNeighborsCache.put(direction, validNeighbors(direction));
        }
    }

    /**
     * For each possible tile, return if it is a valid neighbor to the current tile, in the direction given.
     * @param direction direction to look in
     * @return Set of valid neighbors in the direction given.
     */
    public Set<Tile> validNeighbors(Directions direction) {
        if (validNeighborsCache.get(direction) != null) return validNeighborsCache.get(direction);

        Set<Tile> result = new HashSet<>();
        List<Tile> allPossibleTiles = AllTiles.getCopyAllPossibleTiles();

        for (Tile possibleTile : allPossibleTiles) {
            if (isValidNeighbor(possibleTile, direction)) result.add(possibleTile);
        }
        return result;
    }


    public boolean isValidNeighbor(Tile t, Directions direction) {
        switch (direction) {
            case WEST:
                return sockets[0] == t.sockets[2];
            case NORTH:
                return sockets[1] == t.sockets[3];
            case EAST:
                return sockets[2] == t.sockets[0];
            case SOUTH:
                return sockets[3] == t.sockets[1];
        }
        return false;
    }

    public int stripToSocket(TETile[] strip) {
        int result = 0;
        if (strip[width / 2] == Tileset.FLOOR) result = 1;
        if (strip[2] == Tileset.FLOOR) result = 2;
        if (strip[1] == Tileset.FLOOR) result = 3;
        return result;
    }

    public TETile[] getStrip(Directions direction) {
        TETile[] result = new TETile[shape.length];
        switch (direction) {
            case NORTH:
                for (int i = 0; i < width; i++) {
                    result[i] = shape[i][width - 1];
                }
                break;
            case EAST:
                for (int i = 0; i < width; i++) {
                    result[i] = shape[width - 1][i];
                }
                break;
            case SOUTH:
                for (int i = 0; i < width; i++) {
                    result[i] = shape[i][0];
                }
                break;
            case WEST:
                for (int i = 0; i < width; i++) {
                    result[i] = shape[0][i];
                }
                break;

        }
        return result;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "sockets=" + Arrays.toString(sockets) +
                '}';
    }
}
