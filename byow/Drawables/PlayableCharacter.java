package byow.Drawables;

import byow.TileEngine.Tileset;
import byow.Utilities.Map2D;
import byow.WorldGen.Draw;

import java.io.Serializable;


/**
 * A 1x1 Moveable that can move on Tileset.FLOOR.
 */
public class PlayableCharacter extends Moveable implements Serializable {

    public PlayableCharacter(Container container, int x, int y) {
        super(container, x, y, 1, 1);
        TILES_TO_MOVE_ON.add(Tileset.FLOOR);
    }

    public Map2D.Node currentRoom() {
        Map2D.Node nearest = container.contained.nearest(x, y);
        if (nearest.getArea().contains(x, y)) return nearest;
        return null;
    }






}
