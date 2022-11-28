package byow.Drawables;

import byow.WorldGen.Draw;

/**
 * Specific Container that can contain a Playable Character.
 */
public class Board extends Container {
    PlayableCharacter pc;
    public Board(int width, int height) {
        super(width, height);
    }

    public void setPc(PlayableCharacter pc) {
        if (this.pc != null) Draw.drawShape(this.shape, pc.hiddenTiles, pc.x, pc.y);
        this.pc = pc;
    }

    public PlayableCharacter getPc() {
        return pc;
    }

}
