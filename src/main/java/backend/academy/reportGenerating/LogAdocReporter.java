package backend.academy.reportGenerating;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import static backend.academy.logConversion.LogAnalysis.calculateAverageSize;
import static backend.academy.logConversion.LogAnalysis.calculatePercentile;
import static backend.academy.logConversion.LogAnalysis.getStatusName;

@Log4j2
public class LogAdocReporter implements LogReporter {
    private final String tableBorder = "|===\n";
    private final String bytesSplitter = "b\n";
    private final String lineDivider = "\n";
    private final String statisticBorder = "|===\n\n";
    private final String columnSplitter = " | ";
    private final String header = "[cols=\"1,1\", options=\"header\"]\n";
    private final int topResources = 5;

    @SuppressWarnings("ParameterNumber")
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
        report.append(header);
        report.append(tableBorder);
        report.append("| Метрика | Значение\n");
        report.append("| Файл(-ы) | `").append(logFilePath).append(lineDivider);
        report.append("| Начальная дата | ").append(fromDate).append(lineDivider);
        report.append("| Конечная дата | ").append(toDate).append(lineDivider);
        report.append("| Количество запросов | ").append(requestCount).append(lineDivider);
        report.append("| Средний размер ответа | ")
            .append(requestCount > 0 ? calculateAverageSize(bodyBytesSentCount, requestCount) : 0)
            .append(bytesSplitter);
        report.append("| 95p размера ответа | ");
        if (bodyBytesSentCount > 0) {
            report.append(calculatePercentile(bodyBytesSentList))
                .append(bytesSplitter);

        } else {
            report.append(0).append(bytesSplitter);
        }
        report.append(statisticBorder);

        report.append("== Запрашиваемые ресурсы\n\n");
        report.append(header);
        report.append(tableBorder);
        report.append("| Ресурс | Количество\n");
        List<Map.Entry<String, Integer>> sortedResources = new ArrayList<>(resourceCount.entrySet());
        sortedResources.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(topResources, sortedResources.size()); i++) {
            Map.Entry<String, Integer> entry = sortedResources.get(i);
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append(lineDivider);
        }
        report.append(statisticBorder);

        report.append("== Коды ответа\n\n");
        report.append("[cols=\"1,1,1\", options=\"header\"]\n");
        report.append(tableBorder);
        report.append("| Код | Имя | Количество\n");
        for (Map.Entry<Integer, Integer> entry : statusCount.entrySet()) {
            String statusName = getStatusName(entry.getKey());
            report.append("| ").append(entry.getKey()).append(columnSplitter)
                    .append(statusName).append(columnSplitter).append(entry.getValue()).append(lineDivider);
        }
        report.append(tableBorder);

        try (FileWriter reportFile = new FileWriter("src/main/java/backend/academy/reports/adocReport.adoc")) {
            reportFile.write(String.valueOf(report));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
