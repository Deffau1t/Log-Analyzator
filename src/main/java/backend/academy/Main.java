package backend.academy;

import java.util.Date;
import lombok.experimental.UtilityClass;
import static backend.academy.NginxLogParser.parseDate;
import static backend.academy.NginxLogParser.parseLogLine;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        String logLine = "\"127.0.0.1\" \"-\" \"20/Oct/2023:12:34:56 +0000\" \"GET /index.html HTTP/1.1\" \"200\" \"1234\" \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\"";
        Date startDate = parseDate("21/Oct/2023:12:00:00 +0000");
        Date endDate = parseDate("21/Oct/2023:13:00:00 +0000");
        NginxLogEntity logEntry = parseLogLine(logLine, startDate, endDate);
        System.out.println(logEntry);
    }
}
