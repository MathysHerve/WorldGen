package byow;

import byow.TileEngine.TETile;
import byow.WorldGen.Draw;

import java.util.ArrayList;
import java.util.List;

public class AllTiles {

    public static final List<Tile> allPossibleTiles = new ArrayList<>(){{
        add(roomUD());
        add(roomLR());
        add(roomUDLR());
        add(roomD());
        add(roomR());
        add(roomU());
        add(roomL());
        add(empty());

    }};

    public static TETile[][] getEmptyCanvas() {
        TETile[][] empty = new TETile[Tile.width][Tile.width];
        Draw.fillNothing(empty);
        return empty;
    }

    public static Tile roomUD() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoom(canvas, 7, 7,0, 0);
        return new Tile(canvas);
    }

    public static Tile roomR() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoomDown(canvas, 7, 7, 0, 0);
        return new Tile(Draw.rotateLeft(Draw.rotateLeft(Draw.rotateLeft(canvas))));
    }

    public static Tile roomD() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoomDown(canvas, 7, 7, 0, 0);
        return new Tile(canvas);
    }

    public static Tile roomU() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoomDown(canvas, 7, 7, 0, 0);
        return new Tile(Draw.rotateLeft(Draw.rotateLeft(canvas)));
    }

    public static Tile roomL() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoomDown(canvas, 7, 7, 0, 0);
        return new Tile(Draw.rotateLeft(canvas));
    }

    public static Tile roomLR() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawRoom(canvas, 7, 7, 0, 0);
        return new Tile(Draw.rotateLeft(canvas));
    }

    public static Tile roomUDLR() {
        TETile[][] canvas = getEmptyCanvas();
        Draw.drawOpenRoom(canvas, 7, 7, 0, 0);
        return new Tile(canvas);
    }

    public static Tile empty() {
        TETile[][] empty = getEmptyCanvas();
        return new Tile(getEmptyCanvas());
    }

    public static List<Tile> getCopyAllPossibleTiles() {
        return new ArrayList<>(allPossibleTiles);
    }

}
