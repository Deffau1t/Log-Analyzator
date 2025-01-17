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
public class LogMarkdownReporter implements LogReporter {
    private final String lineDivider = " |\n";
    private final String bytesSplitter = "b |\n\n";
    private final String columnSplitter = " | ";

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

        report.append("# Nginx Log Analysis Report\n\n");
        report.append("## Общая информация\n\n");
        report.append("| Метрика | Значение |\n");
        report.append("|:--------:|---------:|\n");
        report.append("| Файл(-ы) | `").append(Paths.get(logFilePath).getFileName()).append(lineDivider);
        report.append("| Начальная дата | ").append(fromDate).append(lineDivider);
        report.append("| Конечная дата | ").append(toDate).append(lineDivider);
        report.append("| Количество запросов | ").append(requestCount).append(lineDivider);
        report.append("| Средний размер ответа | ")
            .append(requestCount > 0 ? calculateAverageSize(bodyBytesSentCount, requestCount) : 0).append("b |\n");

        report.append("| 95p размера ответа | ");
        if (bodyBytesSentCount > 0) {
            report.append(calculatePercentile(bodyBytesSentList)).append(bytesSplitter);
        } else {
            report.append(0).append(bytesSplitter);
        }

        report.append("## Запрашиваемые ресурсы\n\n");
        report.append("| Ресурс | Количество |\n");
        report.append("|:-------:|-----------:|\n");
        reportingResources(resourceCount, report);
        report.append("\n");

        report.append("## Коды ответа\n\n");
        report.append("| Код | Имя | Количество |\n");
        report.append("|:----:|:----:|-----------:|\n");
        for (Map.Entry<Integer, Integer> entry : statusCount.entrySet()) {
            String statusName = getStatusName(entry.getKey());
            report.append("| ").append(entry.getKey()).append(columnSplitter).append(statusName).append(columnSplitter)
                .append(entry.getValue()).append(lineDivider);
        }
        report.append("\n");

        report.append("## Запрашиваемые методы HTTP\n\n");
        report.append("| Метод HTTP | Количество |\n");
        report.append("|:-------:|:-----------:|\n");
        for (Map.Entry<String, Integer> entry : requestType.entrySet()) {
            report.append("| ").append(entry.getKey()).append(columnSplitter)
                .append(entry.getValue()).append(lineDivider);
        }
        report.append("\n");

        report.append("## Запрашиваемые адреса\n\n");
        report.append("| Адрес | Количество |\n");
        report.append("|:------:|:-----------:|\n");
        reportingAddresses(addressCount, report);

        try (OutputStreamWriter reportFile =
                 new OutputStreamWriter(new FileOutputStream(
                     "src/main/java/backend/academy/reports/markdownReport.md"), StandardCharsets.UTF_8)) {
            reportFile.write(String.valueOf(report));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
