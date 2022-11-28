package byow.GUI;

import byow.Core.Engine;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;


public class MainMenu implements Menu {
    SeedMenu seedMenu;
    InputSource is;
    Engine e;
    boolean display;

    public MainMenu(Engine e, InputSource is) {
        this.is = is;
        this.e = e;
        this.seedMenu = new SeedMenu(e, is, this);
        display = is instanceof KeyboardInputSource;
    }

    public void enter() {
        draw();
        while (is.possibleNextInput()) {
            char c = is.getNextKey();

            if (c == 'N') {
                seedMenu.enter();
                break;
            }
            if (c == 'L') {
                e.loadGame();
                e.playGame(is);
                break;
            }

            if (c == 'Q') {
                break;
            }
        }
    }


    public void draw() {
        if (!display) return;
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(Engine.BIGFONT);

        Point title = new Point(Engine.WIDTH / 2, Engine.HEIGHT * 3 / 4);
        StdDraw.text(title.x, title.y, "My Head Hurts!");

        int fontSmallSize = 18;
        Font fontSmall = new Font("Monaco", Font.BOLD, fontSmallSize);
        StdDraw.setFont(fontSmall);

        Point newGame = new Point(Engine.WIDTH / 2, Engine.HEIGHT / 2);
        Point loadGame = new Point(newGame);
        loadGame.translate(0, -2);
        Point quit = new Point(loadGame);
        quit.translate(0, -2);


        StdDraw.text(newGame.x, newGame.y, "New Game (N)");
        StdDraw.text(loadGame.x, loadGame.y, "Load Game (L)");
        StdDraw.text(quit.x, quit.y, "Quit (Q)");

        StdDraw.show();
    }

    public void back() {

    }


}
