package byow.GUI;

import byow.Core.Engine;
import byow.Drawables.Board;
import byow.Drawables.PlayableCharacter;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.Utilities.Interval2D;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class SeedMenu implements Menu {
    InputSource is;
    Engine e;
    Menu back;
    boolean display;

    public SeedMenu(Engine e, InputSource is, Menu back) {
        this.is = is;
        this.e = e;
        this.back = back;
        display = is instanceof KeyboardInputSource;
    }

    public void enter() {
        String result = "";

        drawMenu();

        while (is.possibleNextInput()) {
            char c = is.getNextKey();

            if (c == '\u001B') {
                back();
                break;
            }

            if (c == 'S') {
                e.reset();
                e.gw.setSeed(Long.parseLong(result));
                Board b = e.board;
                e.gw.genRooms(b, 20, 6, 5);
                Point randPlayableCoord = randPlayerCoord(b);
                e.board.setPc(new PlayableCharacter(b, randPlayableCoord.x, randPlayableCoord.y));
                e.playGame(is);
                break;
            } else if (Character.isDigit(c)){
                result += c;
                drawMenu();
                drawString(result, Engine.BIGFONT, Engine.WIDTH/2, Engine.HEIGHT/2);
                StdDraw.show();
            }
        }

    }

    /**
     * Returns a random coordinate that is inside a room on the board b.
     *
     * @param b Board
     * @return Point (x,y)
     */
    public Point randPlayerCoord(Board b) {
        Point randCoord = e.gw.randomCoord(Engine.WIDTH, Engine.HEIGHT);
        Interval2D randomArea = b.contained.nearest(randCoord.x, randCoord.y).getArea();
        return randomArea.center();
    }

    /**
     * Draws the string s with font f at x and y.
     *
     * @param s The string to draw.
     * @param f The font to use.
     * @param x X Coordinate.
     * @param y Y Coordinate.
     */
    public void drawString(String s, Font f, double x, double y) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        if (!display) return;
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(f);
        StdDraw.text(x, y, s);
        StdDraw.show();

    }


    public void drawMenu() {
        if (!display) return;
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(Engine.BIGFONT);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 4, "SEED: ");
        StdDraw.show();
    }



    public void back() {
        back.enter();
    }
}
