import backend.academy.entities.NginxLogEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import backend.academy.reportGenerating.LogReporter;
import backend.academy.reportGenerating.LogReporterFactory;
import org.junit.jupiter.api.Test;
import static backend.academy.logConversion.FileLogReader.getBufferedReader;
import static backend.academy.logConversion.LogAnalysis.calculateAverageSize;
import static backend.academy.logConversion.LogAnalysis.calculatePercentile;
import static backend.academy.logConversion.NginxLogParser.logDateFilter;
import static backend.academy.logConversion.NginxLogParser.parseLogDate;
import static backend.academy.logConversion.NginxLogParser.parseLogLine;
import static backend.academy.logConversion.NginxLogParser.parseRangeDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LogTests {

    @Test
    public void testReadLogFileFromLocalPath() throws IOException {
        String localPath = "src/main/java/backend/academy/nginx_logs.txt";
        BufferedReader reader = getBufferedReader(localPath);
        assertNotNull(reader);
        reader.close();
    }

    @Test
    public void testReadLogFileFromURL() throws IOException {
        String logUrl =
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";
        BufferedReader reader = getBufferedReader(logUrl);
        assertNotNull(reader);
        reader.close();
    }

    @Test
    @SuppressWarnings("MagicNumbers")
    public void testParsingLogs() {
        String logExample = "93.180.71.3 - - [17/May/2015:08:05:32 +0000]" +
            " \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";
        Date fromDate = parseRangeDate("2014-08-31");
        Date toDate = parseRangeDate("2024-09-01");
        Date checkDate = parseLogDate("17/May/2015:08:05:32 +0000");
        NginxLogEntity logEntry = parseLogLine(logExample, fromDate, toDate);
        assertNotNull(logEntry);
        assertEquals("93.180.71.3", logEntry.remoteAddress());
        assertEquals("-", logEntry.remoteUser());
        assertEquals(checkDate, logEntry.timeLocal());
        assertEquals("GET /downloads/product_1 HTTP/1.1", logEntry.request());
        assertEquals(304, logEntry.status());
        assertEquals(0, logEntry.bodyBytesSent());
        assertEquals("-", logEntry.httpReferer());
        assertEquals("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)", logEntry.httpUserAgent());
    }

    @Test
    public void testTimeFilter() {
        Date fromDate = parseRangeDate("2024-07-31");
        Date toDate = parseRangeDate("2024-09-02");

        Date firstLogDate = parseRangeDate("2024-08-17");
        assertNotNull(logDateFilter(fromDate, toDate, firstLogDate));

        Date secondLogDate = parseRangeDate("2025-08-17");
        assertNull(logDateFilter(fromDate, toDate, secondLogDate));

        Date thirdLogDate = parseRangeDate("2024-09-01");
        assertNotNull(logDateFilter(fromDate, toDate, thirdLogDate));
    }

    @Test
    public void testCalculations() {
        String firstLogExample = "217.168.17.5 - - [17/May/2015:08:05:34 +0000] " +
            "\"GET /downloads/product_1 HTTP/1.1\" 200 490 \"-\" \"Debian APT-HTTP/1.3 (0.8.10.3)\"";
        String secondLogExample = "217.168.17.5 - - [17/May/2015:08:05:02 +0000] " +
            "\"GET /downloads/product_2 HTTP/1.1\" 404 337 \"-\" \"Debian APT-HTTP/1.3 (0.8.10.3)\"";

        Date fromDate = parseRangeDate("2014-07-31");
        Date toDate = parseRangeDate("2016-09-02");
        NginxLogEntity logFirstEntry = parseLogLine(firstLogExample, fromDate, toDate);
        NginxLogEntity logSecondEntry = parseLogLine(secondLogExample, fromDate, toDate);
        List<Long> bodyBytesSentList = new ArrayList<>();

        bodyBytesSentList.add(logFirstEntry.bodyBytesSent());
        bodyBytesSentList.add(logSecondEntry.bodyBytesSent());

        int resourceCount = 2;
        long bodyBytesSentCount = logFirstEntry.bodyBytesSent() + logSecondEntry.bodyBytesSent();

        assertEquals(calculateAverageSize(bodyBytesSentCount, resourceCount), 413);
        assertEquals(calculatePercentile(bodyBytesSentList), 482.35);
    }
}
