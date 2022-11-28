package byow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileLoc{
    int x;
    int y;
    List<Tile> possibilities = AllTiles.getCopyAllPossibleTiles();

    TileLoc(int nx, int ny){
        x= nx;
        y = ny;
        if (y == 0) possibilities.removeIf(t -> (t.sockets[3] == 1));
        if (y == WaveFunctionCollapse.HEIGHT - 1) possibilities.removeIf(t -> (t.sockets[1] == 1));
        if (x == 0) possibilities.removeIf((t -> (t.sockets[0] == 1)));
        if (x == WaveFunctionCollapse.WIDTH - 1) possibilities.removeIf(t -> (t.sockets[2] == 1));
    }

    public List<Tile> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(List<Tile> newPossibilities) {
        possibilities = newPossibilities;
    }

    public double getEntropy() {
        return possibilities.size();
    }

    public String toString() {
        return "TileLoc{" +
                "x=" + x +
                ", y=" + y +
                ", possibilities=" + possibilities +
                '}';
    }

    public boolean equals(TileLoc o){
        int ox = o.x;
        int oy = o.y;
        return x == ox && y == oy;
    }
}
