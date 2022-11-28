package byow.Utilities;

import java.io.Serializable;
import java.util.Comparator;

public class Compare1D implements Comparator<Interval1D>, Serializable {

    @Override
    public int compare(Interval1D o1, Interval1D o2) {
        double o1center = (o1.max() + o1.min()) / 2;
        double o2center = (o2.max() + o1.min()) / 2;

        if (o1center < o2center) {
            return -1;
        } else if (o1center > o2center){
            return 1;
        }
        return 0;
    }
}
