package byow.Core;

import byow.Drawables.*;
import byow.GUI.*;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.Utilities.Interval2D;
import byow.Utilities.Map2D;
import byow.WorldGen.Draw;
import byow.WorldGen.GenerateWorld;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class Engine {
    public TERenderer ter = new TERenderer();
    public GenerateWorld gw = new GenerateWorld();
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;
    public Board board = new Board(WIDTH, HEIGHT);
    public Board hiddenBoard = new Board(WIDTH, HEIGHT);

    public final List<GUIElement> GUI_ELEMENTS = new LinkedList<>();
    public static final Font BIGFONT = new Font("Monaco", Font.BOLD, 30);
    public static final Font SMALLFONT = new Font("Monaco", Font.BOLD, 18);
    public static final String MOVEMENT_CHARS = "WASD ";


    public static void main(String[] args) {
        Engine e = new Engine();
        e.interactWithKeyboard();

    }

    public Engine() {
        ter.initialize(WIDTH, HEIGHT);
    }

    /**
     * Sets the active board to a completely new instance.
     */
    public void reset() {
        board = new Board(WIDTH, HEIGHT);
    }

    /**
     * Initializes the GUI if necessary, and handles all inputs to alter the game board.
     * @param is InputSource to use
     */
    public void playGame(InputSource is) {
        LinkedList<GUIElement> GUIElements = new LinkedList<>();

        BoardGUI boardGUI = new BoardGUI(this, this.board);
        boardGUI.show();
        BoardGUI hiddenBoardGUI = new BoardGUI(this, hiddenBoard);
        hiddenBoardGUI.hide();
        HUDGUI hudGUI = new HUDGUI(this, board);
        hudGUI.show();

        if (is instanceof KeyboardInputSource) {
            GUIElements.add(boardGUI);
            GUIElements.add(hiddenBoardGUI);
            GUIElements.add(hudGUI);
        }

        while (is.possibleNextInput()) {
            char c = is.getNextKey();
            handleMovements(c);

            if (c == 'F') {
                boardGUI.toggle();
                hiddenBoardGUI.toggle();
            }

            if (c == 'P') {
                saveGame();
                new PopUpMenu(this, is, "Saved Game").enter();
            }

            if (c == '\u001B') {
                new MainMenu(this, is).enter();
                break;
            }
            if (c == 'Q') {
                saveGame();
                return;
            }
            updateHidden();
            for (GUIElement element : GUIElements) element.draw();

        }
    }

    /**
     * Will update the hidden Board to be Tileset.NOTHING everywhere. Then it selects an area to be a mirror
     * of the active Board. That way we have a board that is just a small selection of the active Board.
     */
    private void updateHidden() {
        Spotlight spotlight = new Spotlight();
        PlayableCharacter pc = board.getPc();
        int halfWidth = spotlight.getWidth()/2;
        int halfHeight = spotlight.getHeight()/2;

        TETile[][] selection = Draw.select(board.getShape(), spotlight.getShape(),
                pc.x - halfWidth, pc.y - halfHeight);
        Draw.fillNothing(hiddenBoard.getShape());
        Draw.drawShape(hiddenBoard.getShape(), selection,
                pc.x - halfWidth, pc.y - halfHeight);
    }

    /**
     * Takes an input character and will displace the Playable Character accordingly.
     * @param c an input character.
     */
    public void handleMovements(char c) {

        if (MOVEMENT_CHARS.indexOf(c) == -1) return;

        switch (c) {
            case 'W':
                board.getPc().move(Moveable.NORTH);
                break;
            case 'D':
                board.getPc().move(Moveable.EAST);
                break;
            case 'S':
                board.getPc().move(Moveable.SOUTH);
                break;
            case 'A':
                board.getPc().move(Moveable.WEST);
                break;
        }
    }



    private void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("t.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(board);
            oos.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void loadGame() {
        try {
            FileInputStream fis = new FileInputStream("t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            board = (Board) ois.readObject();
            ois.close();
        } catch (Exception e) {

        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        final StringInputDevice inputSource = new StringInputDevice(input);
        MainMenu mmenu = new MainMenu(this, inputSource);
        mmenu.enter();
        return board.getShape();
    }


    /**
     * Starts the game and listens to the keyboard for input. GUI will be drawn with this mode.
     */
    public void interactWithKeyboard() {
        final KeyboardInputSource inputSource = new KeyboardInputSource();
        MainMenu mmenu = new MainMenu(this, inputSource);
        mmenu.enter();
    }

}
