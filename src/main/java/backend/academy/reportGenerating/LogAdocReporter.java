package backend.academy.reportGenerating;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import static backend.academy.logConversion.LogAnalysis.calculateAverageSize;
import static backend.academy.logConversion.LogAnalysis.calculatePercentile;
import static backend.academy.logConversion.LogAnalysis.reportingAddresses;
import static backend.academy.logConversion.LogAnalysis.reportingResources;
import static backend.academy.logConversion.LogFieldHandler.getStatusName;

@Log4j2
public class LogAdocReporter implements LogReporter {
    private final String tableBorder = "|===\n";
    private final String bytesSplitter = "b\n";
    public static final String LINEDIVIDER = "\n";
    private final String statisticBorder = "|===\n\n";
    private final String columnSplitter = " | ";
    private final String header = "[cols=\"1,1\", options=\"header\"]\n";
    public static final int TOPVALUES = 5;

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
        Map<Integer, Integer> statusCount,
        Map<String, Integer> requestType,
        Map<String, Integer> addressCount
    ) {
        StringBuilder report = new StringBuilder();

        report.append("= Nginx Log Analysis Report\n\n");
        report.append("== Общая информация\n\n");
        report.append(header);
        report.append(tableBorder);
        report.append("| Метрика | Значение\n");
        report.append("| Файл(-ы) | `").append(Paths.get(logFilePath).getFileName()).append(LINEDIVIDER);
        report.append("| Начальная дата | ").append(fromDate).append(LINEDIVIDER);
        report.append("| Конечная дата | ").append(toDate).append(LINEDIVIDER);
        report.append("| Количество запросов | ").append(requestCount).append(LINEDIVIDER);
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
        reportingResources(resourceCount, report);
        report.append(statisticBorder);

        report.append("== Коды ответа\n\n");
        report.append("[cols=\"1,1,1\", options=\"header\"]\n");
        report.append(tableBorder);
        report.append("| Код | Имя | Количество\n");
        for (Map.Entry<Integer, Integer> entry : statusCount.entrySet()) {
            String statusName = getStatusName(entry.getKey());
            report.append("| ").append(entry.getKey()).append(columnSplitter)
                    .append(statusName).append(columnSplitter).append(entry.getValue()).append(LINEDIVIDER);
        }
        report.append(statisticBorder);

        report.append("== Запрашиваемые методы HTTP\n\n");
        report.append(header);
        report.append(tableBorder);
        report.append("| Метод HTTP | Количество\n");
        for (Map.Entry<String, Integer> entry : requestType.entrySet()) {
            report.append("| ").append(entry.getKey()).append(columnSplitter)
                .append(entry.getValue()).append(LINEDIVIDER);
        }
        report.append(statisticBorder);

        report.append("== Запрашиваемые адреса\n\n");
        report.append(header);
        report.append(tableBorder);
        report.append("| Адрес | Количество\n");
        reportingAddresses(addressCount, report);
        report.append(tableBorder);

        try (OutputStreamWriter reportFile = new OutputStreamWriter(new FileOutputStream(
            "src/main/java/backend/academy/reports/adocReport.adoc"), StandardCharsets.UTF_8)) {
            reportFile.write(String.valueOf(report));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
