package byow.Utilities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Interval2D implements Serializable {
    public Interval1D x;
    public Interval1D y;
    public Interval2D(int xmin, int xmax, int ymin, int ymax) {
        this.x = new Interval1D(xmin, xmax);
        this.y = new Interval1D(ymin, ymax);
    }

    public boolean intersects(Interval2D that) {
        return (this.x.intersects(that.x) && this.y.intersects(that.y));
    }

    public boolean contains(int x, int y) {
        return this.x.contains(x) && this.y.contains(y);
    }

    public Point center() {
        return new Point((int) ((x.min() + x.max()) / 2), (int) ((y.min() + y.max()) / 2));
    }

    public boolean isLeft(Interval2D that) {
        return that.x.min() <= this.x.min();
    }
    public boolean isRight(Interval2D that) {
        return this.x.min() < that.x.min();
    }
    public boolean isTop(Interval2D that) {
        return this.y.min() < that.y.min();
    }
    public boolean isBottom(Interval2D that) {
        return that.y.min() <= this.y.min();
    }

    public double distance(Interval2D that) {
        if (this.intersects(that)) {
            return 0;
        }

        boolean left, right, top, bottom;

        left = isLeft(that);
        right = isRight(that);
        top = isTop(that);
        bottom = isBottom(that);

        if (top && right) {
            return Point2D.distance(this.x.max(), this.y.max(), that.x.min(), that.y.min());
        }
        if (bottom & right) {
            return Point2D.distance(this.x.max(), this.y.min(), that.x.min(), that.y.max());
        }
        if (top && left) {
            return Point2D.distance(this.x.min(), this.y.max(), that.x.max(), that.y.min());
        }
        if (bottom && left) {
            return Point2D.distance(this.x.min(), this.y.min(), that.x.max(), that.y.max());
        }
        if (left) {
            return this.x.min() - that.x.max();
        }
        if (right) {
            return that.x.min() - this.x.max();
        }
        if (top) {
            return that.y.min() - this.y.max();
        }
        if (bottom) {
            return this.y.min() - that.y.max();
        }
        else {
            return 0;
        }
    }

    public String toString() {
        return "Interval2D: " + x.toString() + " " + y.toString();
    }

    public static void main(String[] args) {
        Interval2D r1 = new Interval2D(0, 5, 0, 5);
        Interval2D r2 = new Interval2D(6, 11, 8, 11);

        System.out.println(r1.distance(r2));
    }


}
