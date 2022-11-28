package byow.Utilities;

import java.io.Serializable;
import java.util.Comparator;

public class DistanceComparator implements Comparator<Interval2D>, Serializable {
    private Interval2D ref;

    public DistanceComparator(Interval2D ref) {
        this.ref = ref;
    }
    @Override
    public int compare(Interval2D o1, Interval2D o2) {
        double dist1 = o1.distance(ref);
        double dist2 = o2.distance(ref);
        return Double.compare(dist1, dist2);
    }
}
