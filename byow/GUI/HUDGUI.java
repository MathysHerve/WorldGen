package byow.GUI;

import byow.Core.Engine;
import byow.Drawables.Board;
import byow.TileEngine.TETile;
import byow.WorldGen.Draw;
import byow.Utilities.Map2D;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

/**
 * A GUIElement that displays game information.
 * @author Mathys Herve
 */
public class HUDGUI implements GUIElement {
    private boolean display = false;
    private Engine engine;
    private Board board;

    public HUDGUI(Engine e, Board b) {
        this.engine = e;
        this.board = b;
    }

    /**
     * Displays HUD
     */
    public void show() {
        display = true;
    }

    /**
     * Does not display HUD
     */
    public void hide() {
        display = false;
    }

    /**
     * Toggles display on/off
     */
    public void toggle() {
        display = !display;
    }

    /**
     * Draws HUD onto StdDraw
     */
    public void draw() {
        if (!display) return;
        int mousex = (int) StdDraw.mouseX();
        int mousey = (int) StdDraw.mouseY();
        String text = "";

        StdDraw.setPenColor(Color.white);

//        if (Draw.validPoint(board.getShape(), mousex, mousey)) {
//            Map2D.Node nearest = board.contained.nearest(mousex, mousey);
//            if (nearest.getArea().contains(mousex, mousey)) text = nearest.getValue().toString();
//            TETile mousedTile = board.getShape()[mousex][mousey];
//            String text = mousedTile.toString();
        Map2D.Node nearest = board.getPc().currentRoom();
        if (nearest != null) text = nearest.getValue().toString();
        StdDraw.setFont(Engine.SMALLFONT);
        StdDraw.textLeft(2, Engine.HEIGHT - 2, text);
        StdDraw.show();
//        }
    }
}
