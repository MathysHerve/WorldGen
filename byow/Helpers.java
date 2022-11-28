package byow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Helpers {
    public static final Map<Integer, Directions> DIRECTIONS_INTEGER_MAP = new HashMap<>(){{
        int i = 0;
        for (Directions direction : Directions.values()) {
            put(i, direction);
            i++;
        }
    }};
}
