package byow.Utilities;

import byow.Drawables.Room;

import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Map2D<Value> implements Map<Interval2D, Value>, Serializable {
    public class Node implements Serializable {
        Interval2D area;
        Node left, right;
        Interval1D[] sides = new Interval1D[2];
        Value v;
        public Node(Interval2D area, Value v) {
            this.sides[0] = area.x;
            this.sides[1] = area.y;
            this.v = v;
            this.area = area;

        }
        public Value getValue() {
            return v;
        }
        public Interval2D getArea() {
            return area;
        }
    }
    private static final int X = 0;
    private static final int Y = 1;
    private Node root;
    private final List<Interval2D> keys = new ArrayList<>();
    private final List<Value> values = new ArrayList<>();
    private final Set<Entry<Interval2D, Value>> entries = new HashSet<>();
    private final int k = 2;
    private final Compare1D Compare1D = new Compare1D();
    public int size = 0;


    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size <= 0;
    }
    @Override
    public boolean containsKey(Object key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object v) {
        return values.contains(v);
    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }
    @Override
    public Set<Interval2D> keySet() {
        return new LinkedHashSet<>(keys);
    }

    @Override
    public Collection<Value> values() {
        return values;
    }

    @Override
    public Set<Entry<Interval2D, Value>> entrySet() {
        return entries;
    }




    @Override
    public Value remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Interval2D, ? extends Value> m) {
        throw new UnsupportedOperationException();
    }


    public Value put(Interval2D area, Value v) {
        root = put(root, area, v, 0);
        return v;
    }
    public Node put(Node node, Interval2D area, Value v, int depth) {
        int axis = depth % k;
        if (node == null) {
            keys.add(area);
            values.add(v);
            entries.add(new AbstractMap.SimpleEntry<>(area, v));
            size += 1;
            return new Node(area, v);
        }

        if (area.intersects(node.area)) return node;
        if (Compare1D.compare(getD(axis, area), node.sides[axis]) < 0) {
            node.left = put(node.left, area, v, depth + 1);
        } else {
            node.right = put(node.right, area, v, depth + 1);
        }
        return node;
    }

    public Value get(Object key) {
        if (key instanceof Interval2D) {
            return get(root, (Interval2D) key, 0);
        } else {
            return null;
        }

    }

    private Value get(Node node, Interval2D key, int depth) {
        int axis = depth % k;
        if (node == null) return null;
        if (node.area.equals(key)) return node.v;
        if (Compare1D.compare(getD(axis, key), node.sides[axis]) < 0) {
            return get(node.left, key, depth + 1);
        }
        if (Compare1D.compare(getD(axis, key), node.sides[axis]) > 0) {
            return get(node.right, key, depth + 1);
        }
        return null;
    }

    public Value get(int x, int y) {
        Node nearest = nearest(new Interval2D(x, x, y, y));
        if (nearest.area.contains(x, y)) return nearest.v;
        return null;
    }

    public List<Interval2D> slowNearest(Interval2D area) {
        Collections.sort(keys, new DistanceComparator(area));
        return keys;
    }

    public Node nearest(Interval2D area) {
        return nearest(root, area, root, 0);
    }

    public Node nearest(int x, int y) {
        return nearest(root, new Interval2D(x, x, y, y), root, 0);
    }

    public Node nearest(Node node, Interval2D goal, Node best, int depth) {
        final int axis = depth % k;
        if (node == null) return best;
        if (node.area.distance(goal) < best.area.distance(goal)) {
            best = node;
        }
        Node goodSide, badSide;
        if (Compare1D.compare(getD(axis, goal), node.sides[axis]) < 0) {
            goodSide = node.left;
            badSide = node.right;
        } else {
            goodSide = node.right;
            badSide = node.left;
        }
        best = nearest(goodSide, goal, best, depth + 1);
        Interval2D possibleBest = possibleBest(axis, node, goal);
        if (possibleBest.distance(goal) < best.area.distance(goal)) {
            best = nearest(badSide, goal, best, depth + 1);
        }

        return best;

    }

    private Interval2D possibleBest(int axis, Node current_node, Interval2D goal) {
        Interval2D possibleBest;
        if (axis == X) {
            possibleBest = new Interval2D((int) current_node.area.x.min() , (int) current_node.area.x.max() , (int) goal.y.min(), (int) goal.y.max());
        } else {
            possibleBest = new Interval2D((int) goal.x.min() , (int) goal.x.max() , (int) current_node.area.y.min(), (int) current_node.area.y.max());
        }
        return possibleBest;
    }

    private Interval1D getD(int dimension, Interval2D area) {
        if (dimension == X) {
            return area.x;
        } else {
            return area.y;
        }
    }


    public static void main(String[] args) {
        Map2D rqt = new Map2D();
        Room blank1 = new Room(19, 8);
        Interval2D i1 = new Interval2D( 7, 25, 10, 17);
        rqt.put(i1, blank1);

        System.out.println(rqt.get(8, 11));

    }


}
