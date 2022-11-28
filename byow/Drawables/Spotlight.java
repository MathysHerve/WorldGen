package byow.Drawables;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGen.Draw;

import java.util.LinkedList;

public class Spotlight extends Drawable {
    public Spotlight() {
        super(11, 11);
        drawLayers(6);

    }

    private void drawLayers(int n) {
        LinkedList<TETile> tiles = new LinkedList<>();
        tiles.add(Tileset.FLOWER);
        tiles.add(Tileset.FLOWER);
        tiles.add(Tileset.FLOWER);
        tiles.add(Tileset.GRASS);
        tiles.add(Tileset.GRASS);
        tiles.add(Tileset.TREE);
        int centerx = 11 / 2;
        for (int i = 0; i < n; i++) {
            this.size += Draw.drawRectangle(shape, tiles.get(i), 2*i + 1, 2*i + 1, centerx - i, centerx - i);
        }
    }

    public static void main(String[] args) {
        Spotlight sp = new Spotlight();
        sp.draw();
    }
}
