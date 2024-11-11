package backend.academy.logConversion;

import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import static backend.academy.reportGenerating.LogAdocReporter.LINEDIVIDER;
import static backend.academy.reportGenerating.LogAdocReporter.TOPRESOURCES;

@SuppressWarnings("MagicNumber")
public class LogAnalysis {
    private LogAnalysis() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void updateResourceCount(Map<String, Integer> resourceCount, String request) {
        String resource = request.split(" ")[1];
        resourceCount.put(resource, resourceCount.getOrDefault(resource, 0) + 1);
    }

    public static void updateStatusCount(Map<Integer, Integer> statusCount, int status) {
        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
    }

    public static long calculateAverageSize(long bodyBytesSentCount, int requestCount) {
        return bodyBytesSentCount / requestCount;
    }

    public static double calculatePercentile(List<Long> bodyBytesSentList) {
        return Quantiles.percentiles().index(95).compute(bodyBytesSentList);
    }

    public static String getStatusName(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 206 -> "Partial Content";
            case 301 -> "Moved Permanently";
            case 304 -> "Not Modified";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 416 -> "Requested Range Not Satisfiable";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

    public static void reportingResources(Map<String, Integer> resourceCount, StringBuilder report) {
        List<Map.Entry<String, Integer>> sortedResources = new ArrayList<>(resourceCount.entrySet());
        sortedResources.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(TOPRESOURCES, sortedResources.size()); i++) {
            Map.Entry<String, Integer> entry = sortedResources.get(i);
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append(LINEDIVIDER);
        }
    }
}
