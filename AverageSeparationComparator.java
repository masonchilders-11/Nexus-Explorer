import java.util.Comparator;
import java.util.Map;

/**
 * Comparator function for a given string based on their average separation
 *
 * @author Mason Childers, CS10, 23W
 */
public class AverageSeparationComparator implements Comparator<String> {

    private Map<String, Double> map;

    public AverageSeparationComparator(Map<String, Double> map) {
        this.map = map;
    }

    /**
     * Compares two strings based on their average separation
     *
     * @param s1 the first object to be compared
     * @param s2 the second object to be compared
     */
    @Override
    public int compare(String s1, String s2) {
        double separation1 = map.get(s1);
        double separation2 = map.get(s2);
        double difference = separation1 - separation2;

        if (difference < 0) return -1;
        else if (difference == 0) return 0;
        else return 1;
    }
}
