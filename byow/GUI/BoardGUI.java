package byow.GUI;

import byow.Core.Engine;
import byow.Drawables.Board;

public class BoardGUI implements GUIElement {
    public Board board;
    Engine e;
    boolean display = false;

    public BoardGUI(Engine e, Board board) {
        this.board = board;
        this.e = e;
    }

    public void show() {
        display = true;
    }

    public void hide() {
        display = false;
    }

    @Override
    public void toggle() {
        display = !display;
    }

    public void draw() {
        if (display) e.ter.renderFrame(board.getShape());
    }
}
