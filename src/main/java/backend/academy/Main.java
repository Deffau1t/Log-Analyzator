package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import backend.academy.entities.NginxLogEntity;
import backend.academy.reportGenerating.LogReporter;
import backend.academy.reportGenerating.LogReporterFactory;
import lombok.experimental.UtilityClass;
import static backend.academy.FileLogReader.getBufferedReader;
import static backend.academy.LogAnalysis.updateResourceCount;
import static backend.academy.LogAnalysis.updateStatusCount;
import static backend.academy.NginxLogParser.parseDate;
import static backend.academy.NginxLogParser.parseLogLine;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        String stringFromDate = "12/May/2015:08:05:32 +0000";
        String stringToDate = "17/May/2016:08:05:32 +0000";
        Date fromDate = parseDate(stringFromDate);
        Date toDate = parseDate(stringToDate);
        String logFilePath = "src/main/java/backend/academy/nginx_logs.txt";

        Map<String, Integer> resourceCount = new HashMap<>();
        Map<Integer, Integer> statusCount = new HashMap<>();
        List<Long> bodyBytesSentList = new ArrayList<>();
        long bodyBytesSentCount = 0;
        int requestCount = 0;

        try (BufferedReader reader = getBufferedReader(logFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                NginxLogEntity logEntry = parseLogLine(line, fromDate, toDate);
                if (logEntry != null) {
                    // Обновляем статистику
                    updateResourceCount(resourceCount, logEntry.request());
                    updateStatusCount(statusCount, logEntry.status());
                    bodyBytesSentList.add(logEntry.bodyBytesSent());
                    bodyBytesSentCount += logEntry.bodyBytesSent();
                    requestCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogReporter reporter = LogReporterFactory.createReporter(1);
        reporter.generateReport(logFilePath, fromDate, toDate,
            requestCount, bodyBytesSentCount, bodyBytesSentList, resourceCount, statusCount);
    }
}
