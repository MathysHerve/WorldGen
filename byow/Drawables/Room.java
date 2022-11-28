package byow.Drawables;

import byow.WorldGen.Draw;

public class Room extends Container {

    public Room(int width, int height) {
        super(width, height);
        this.size += Draw.drawOpenRoom(shape, this.width, this.height, 0, 0);

    }

    public static void main(String[] args) {
        Room r = new Room(7, 7);
        r.draw();
    }

    @Override
    public String toString() {
        return "Room " + width + " " + height;
    }
}
