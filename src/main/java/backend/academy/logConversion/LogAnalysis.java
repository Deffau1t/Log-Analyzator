package backend.academy.logConversion;

import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import static backend.academy.reportGenerating.LogAdocReporter.LINEDIVIDER;
import static backend.academy.reportGenerating.LogAdocReporter.TOPVALUES;


@UtilityClass
public class LogAnalysis {
    // Счетчик компонента request
    public static void updateResourceCount(Map<String, Integer> resourceCount, String request) {
        String resource = request.split(" ")[1];
        resourceCount.put(resource, resourceCount.getOrDefault(resource, 0) + 1);
    }

    public static String getRequestType(String request) {
        return request.split(" ")[0];
    }

    // Счетчик HTTP-статусов кода ответа сервера
    public static void updateStatusCount(Map<Integer, Integer> statusCount, int status) {
        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
    }

    // Подсчет среднего размера файла
    public static long calculateAverageSize(long bodyBytesSentCount, int requestCount) {
        return bodyBytesSentCount / requestCount;
    }

    // Подсчет 95-ого процентиля
    @SuppressWarnings("MagicNumber")
    public static double calculatePercentile(List<Long> bodyBytesSentList) {
        return Quantiles.percentiles().index(95).compute(bodyBytesSentList);
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
}
