package backend.academy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LogAnalysis {
    public static void updateResourceCount(Map<String, Integer> resourceCount, String request) {
        String resource = request.split(" ")[1];
        resourceCount.put(resource, resourceCount.getOrDefault(resource, 0) + 1);
    }

    public static void updateStatusCount(Map<Integer, Integer> statusCount, int status) {
        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
    }

    public static long calculatePercentile(List<Long> values, double percentile) {
        if (values.isEmpty()) {
            return 0;
        }

        Collections.sort(values);
        int n = values.size();
        double percentileIndex = (n - 1) * percentile / 100.0;
        int downRoundedNumber = (int) Math.floor(percentileIndex);
        int upRoundedNumber = (int) Math.ceil(percentileIndex);

        if (downRoundedNumber == upRoundedNumber) {
            return values.get(downRoundedNumber);
        }

        double firstInterpolationPart = values.get(downRoundedNumber) * (upRoundedNumber - percentileIndex);
        double secondInterpolationPart = values.get(upRoundedNumber) * (percentileIndex - downRoundedNumber);
        return (long) (firstInterpolationPart + secondInterpolationPart);
    }
}
