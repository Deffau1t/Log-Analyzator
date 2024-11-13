package backend.academy.logConversion;

import backend.academy.entities.NginxLogEntity;
import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static backend.academy.reportGenerating.LogAdocReporter.LINEDIVIDER;
import static backend.academy.reportGenerating.LogAdocReporter.TOPVALUES;

@SuppressWarnings("MagicNumber")
public class LogAnalysis {
    private LogAnalysis() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void updateResourceCount(Map<String, Integer> resourceCount, String request) {
        String resource = request.split(" ")[1];
        resourceCount.put(resource, resourceCount.getOrDefault(resource, 0) + 1);
    }

    public static String getRequestType(String request) {
        return request.split(" ")[0];
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
        for (int i = 0; i < Math.min(TOPVALUES, sortedResources.size()); i++) {
            Map.Entry<String, Integer> entry = sortedResources.get(i);
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append(LINEDIVIDER);
        }
    }

    public static void reportingAddresses(Map<String, Integer> addressCount, StringBuilder report) {
        List<Map.Entry<String, Integer>> sortedAddresses = new ArrayList<>(addressCount.entrySet());
        sortedAddresses.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(TOPVALUES, sortedAddresses.size()); i++) {
            Map.Entry<String, Integer> entry = sortedAddresses.get(i);
            report.append("|  `").append(entry.getKey()).append("` |").append(entry.getValue()).append(LINEDIVIDER);
        }
    }

    public static boolean isApproachToFilters(String filterField, String filterValue, NginxLogEntity logEntry) {
        if (filterValue != null) {
            Pattern pattern = Pattern.compile(filterValue);
            if (filterField != null) {
                String logEntryValue = getField(filterField, logEntry);
                if (logEntryValue == null) {
                    return false;
                }
                return pattern.matcher(logEntryValue).find();
            } else {
                return pattern.matcher(logEntry.toString()).find();
            }
        } else {
            return true;
        }

    }

    @SuppressWarnings("ReturnCount")
    private static String getField(String filterField, NginxLogEntity logEntity) {
        switch (filterField) {
            case "remoteAddress" -> {
                return logEntity.remoteAddress();
            }
            case "remoteUser" -> {
                return logEntity.remoteUser();
            }
            case "timeLocal" -> {
                return logEntity.timeLocal().toString();
            }
            case "request" -> {
                return logEntity.request();
            }
            case "status" -> {
                return String.valueOf(logEntity.status());
            }
            case "bodyBytesSent" -> {
                return String.valueOf(logEntity.bodyBytesSent());
            }
            case "httpReferer" -> {
                return logEntity.httpReferer();
            }
            case "httpUserAgent" -> {
                return logEntity.httpUserAgent();
            }
            default -> {
                return null;
            }
        }
    }
}
