package backend.academy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LogRendering {
    public static void printTopResources(Map<String, Integer> resourceCounts, int limit) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(resourceCounts.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (int i = 0; i < Math.min(limit, sortedEntries.size()); i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue() + " requests");
        }
    }

    public static void printStatusCount(Map<Integer, Integer> statusCounts) {
        for (Map.Entry<Integer, Integer> entry : statusCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " responses");
        }
    }
}
