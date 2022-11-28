package byow;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.WorldGen.Draw;

import java.util.*;

public class WaveFunctionCollapse {
    public static final int WIDTH = 13;
    public static final int HEIGHT = 6;
    public final Random random = new Random();
    public final TileLoc[][] map = new TileLoc[WIDTH][HEIGHT];


    public static void main(String[] args) {
        WaveFunctionCollapse wfc = new WaveFunctionCollapse();
        wfc.setup(1);
        wfc.run();
        wfc.draw();
    }

    void draw() {
        TETile[][] finalBoard = new TETile[WIDTH * 7][HEIGHT * 7];
        Draw.fillNothing(finalBoard);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                for (Tile possibleTile : map[i][j].getPossibilities()) {
                    Draw.drawShape(finalBoard, possibleTile.shape, i * 7, j * 7);
                }
            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH * 7, HEIGHT * 7);
        ter.renderFrame(finalBoard);
    }

    void run() {
        while (!isCollapsed()) {
            iterate();
        }
    }

    void setup(long seed) {
        random.setSeed(seed);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = new TileLoc(i, j);
            }
        }
    }

    void iterate() {
        TileLoc coordOfMinimumEntropy = getMinEntropy();
        System.out.println("Collapsing " + getMinEntropy());
        collapse(coordOfMinimumEntropy);
        propogate(coordOfMinimumEntropy);
    }

    void constrain(TileLoc coord, Tile t) {
        coord.getPossibilities().remove(t);
    }

    /**
     * @return The location with the least entropy on the entire map. Or a random one, if they are all the same.
     */
    TileLoc getMinEntropy() {
        final TileLoc dummyTile = new TileLoc(0, 0);
        TileLoc minEntropyTileLoc = dummyTile;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (!TileLocCollapsed(map[i][j]) && (map[i][j].getEntropy() < minEntropyTileLoc.getEntropy())) {
                    minEntropyTileLoc = map[i][j];
                }
            }
        }

        if (minEntropyTileLoc == dummyTile) {
            int randxIndex = random.nextInt(WIDTH);
            int randyIndex = random.nextInt(HEIGHT);
            minEntropyTileLoc = map[randxIndex][randyIndex];
        }
        return minEntropyTileLoc;
    }

    boolean isCollapsed() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
//                System.out.println(map[i][j].getPossibilities().size());
                if (!TileLocCollapsed(map[i][j])) return false;
            }
        }
        return true;
    }

    boolean TileLocCollapsed(TileLoc coord) {
        return coord.getPossibilities().size() <= 1;
    }


    void collapse(TileLoc coord) {
        final List<Tile> allLocalPoss = coord.getPossibilities();
        final List<Tile> collapsedPoss = new ArrayList<>();
        int randIndex = random.nextInt(allLocalPoss.size());
        collapsedPoss.add(allLocalPoss.get(randIndex));
        coord.setPossibilities(collapsedPoss);
    }

    List<Directions> validDirections(TileLoc coord) {
        final List<Directions> result = new ArrayList<>();
        if (coord.x > 0) result.add(Directions.WEST);
        if (coord.x < WIDTH - 1) result.add(Directions.EAST);
        if (coord.y > 0) result.add(Directions.SOUTH);
        if (coord.y < HEIGHT - 1) result.add(Directions.NORTH);
        return result;
    }


    void propogate(TileLoc coord) {
        final Stack<TileLoc> stack = new Stack<>();
        stack.push(coord);

        while (!stack.empty()) {
            TileLoc currentLoc = stack.pop();

            for (Directions d : validDirections(currentLoc)) {
                Set<Tile> possibleNeighbors = new HashSet<>();
                for (Tile t : currentLoc.getPossibilities()) {
                    possibleNeighbors.addAll(t.validNeighbors(d));
                }

                TileLoc neighborLoc = getNeighborLoc(currentLoc, d);
                List<Tile> neighborPossTiles = neighborLoc.getPossibilities();
                List<Tile> neighborPossTilesCopy = new ArrayList<>(neighborPossTiles);

                for (Tile neighborPossTile : neighborPossTilesCopy) {
                    if (!possibleNeighbors.contains(neighborPossTile)) {
                        constrain(neighborLoc, neighborPossTile);
                        if (!stack.contains(neighborLoc)) stack.push(neighborLoc);
                    }
                }

            }
        }
    }

    TileLoc getNeighborLoc(TileLoc coord, Directions direction) {
        switch (direction) {
            case EAST:
                return map[coord.x + 1][coord.y];
            case NORTH:
                return map[coord.x][coord.y + 1];
            case SOUTH:
                return map[coord.x][coord.y - 1];
            case WEST:
                return map[coord.x - 1][coord.y];
        }
        return null;
    }
}
