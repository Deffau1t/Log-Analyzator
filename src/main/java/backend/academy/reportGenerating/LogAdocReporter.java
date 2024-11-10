package backend.academy.reportGenerating;

import com.google.common.math.Quantiles;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import static backend.academy.logConversion.LogAnalysis.getStatusName;

public class LogAdocReporter implements LogReporter {
    @Override
    public void generateReport(
        String logFilePath,
        String fromDate,
        String toDate,
        int requestCount,
        long bodyBytesSentCount,
        List<Long> bodyBytesSentList,
        Map<String, Integer> resourceCount,
        Map<Integer, Integer> statusCount
    ) {
        StringBuilder report = new StringBuilder();

        report.append("= Nginx Log Analysis Report\n\n");
        report.append("== Общая информация\n\n");
        report.append("[cols=\"1,1\", options=\"header\"]\n");
        report.append("|===\n");
        report.append("| Метрика | Значение\n");
        report.append("| Файл(-ы) | `").append(logFilePath).append("`\n");
        report.append("| Начальная дата | ").append(fromDate).append("\n");
        report.append("| Конечная дата | ").append(toDate).append("\n");
        report.append("| Количество запросов | ").append(requestCount).append("\n");
        report.append("| Средний размер ответа | ").append(requestCount > 0 ? bodyBytesSentCount / requestCount : 0).append("b\n");
        if (bodyBytesSentCount > 0) {
            report.append("| 95p размера ответа | ").append(Quantiles.percentiles().index(95).
                compute(bodyBytesSentList)).append("b\n");

        } else {
            report.append("| 95p размера ответа | ").append(0).append("b\n");
        }
        report.append("|===\n\n");

        report.append("== Запрашиваемые ресурсы\n\n");
        report.append("[cols=\"1,1\", options=\"header\"]\n");
        report.append("|===\n");
        report.append("| Ресурс | Количество\n");
        List<Map.Entry<String, Integer>> sortedResources = new ArrayList<>(resourceCount.entrySet());
        sortedResources.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(5, sortedResources.size()); i++) {
            Map.Entry<String, Integer> entry = sortedResources.get(i);
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append("\n");
        }
        report.append("|===\n\n");

        report.append("== Коды ответа\n\n");
        report.append("[cols=\"1,1,1\", options=\"header\"]\n");
        report.append("|===\n");
        report.append("| Код | Имя | Количество\n");
        for (Map.Entry<Integer, Integer> entry : statusCount.entrySet()) {
            String statusName = getStatusName(entry.getKey());
            report.append("| ").append(entry.getKey()).append(" | ").append(statusName).append(" | ").append(entry.getValue()).append("\n");
        }
        report.append("|===\n");

        try (FileWriter reportFile = new FileWriter("src/main/java/backend/academy/reports/adocReport.adoc")) {
            reportFile.write(String.valueOf(report));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
