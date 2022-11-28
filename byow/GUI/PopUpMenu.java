package byow.GUI;

import byow.Core.Engine;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class PopUpMenu implements Menu {
    InputSource is;
    Engine e;
    String text;
    boolean display;

    public PopUpMenu(Engine e, InputSource is, String text) {
        this.e = e;
        this.is = is;
        this.text = text;
        display = is instanceof KeyboardInputSource;
    }

    private void drawPopUp() {
        if (!display) return;
        StdDraw.setFont(Engine.BIGFONT);
        StdDraw.setPenColor(Color.darkGray);
        StdDraw.filledRectangle(Engine.WIDTH / 2, Engine.HEIGHT / 2, 8, 3);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2, text);
        StdDraw.show();
    }

    public void enter() {

        drawPopUp();

        while (is.possibleNextInput()) {
            char c = is.getNextKey();
            if (c == 'T') {
                break;
            }
        }
    }

    public void back() {

    }
}
