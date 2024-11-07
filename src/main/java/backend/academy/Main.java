package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import static backend.academy.FileLogReader.getBufferedReader;
import static backend.academy.LogAnalysis.calculatePercentile;
import static backend.academy.LogAnalysis.updateResourceCount;
import static backend.academy.LogAnalysis.updateStatusCount;
import static backend.academy.LogRendering.printStatusCount;
import static backend.academy.LogRendering.printTopResources;
import static backend.academy.NginxLogParser.parseDate;
import static backend.academy.NginxLogParser.parseLogLine;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        String stringFromDate = "2023-10-20T12:00:00Z"; // ISO 8601
        String stringToDate = "2023-11-20T13:00:01Z";
        Date fromDate = parseDate(stringFromDate);
        Date toDate = parseDate(stringToDate);
        String logFilePath = "src/main/java/backend/academy/logFile.log";

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

        // Выводим статистику
        System.out.println("Количество запросов: " + requestCount);
        System.out.println("Наиболее часто запрашиваемые источники:");
        printTopResources(resourceCount, 5);
        System.out.println("Коды ответов:");
        printStatusCount(statusCount);
        System.out.println("Средний размер ответа: " + (requestCount > 0 ? bodyBytesSentCount / requestCount : 0) + " bytes");
        System.out.println("Рассчитывает 95% перцентиль размера ответа сервера: "
            + calculatePercentile(bodyBytesSentList, 95));
    }
}
