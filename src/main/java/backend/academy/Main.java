package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import lombok.experimental.UtilityClass;
import static backend.academy.FileLogReader.getBufferedReader;
import static backend.academy.NginxLogParser.parseDate;
import static backend.academy.NginxLogParser.parseLogLine;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        String stringFromDate = "2023-10-20T12:00:00Z"; // ISO 8601
        String stringToDate = "2023-11-20T12:00:01Z";
        Date fromDate = parseDate(stringFromDate);
        Date toDate = parseDate(stringToDate);
        String logFilePath = "C:\\Users\\mger_\\IdeaProjects\\backend_academy_2024_project_3-java-Deffau1t\\src\\main\\java\\backend\\academy\\logFile.log";

        try {
            BufferedReader reader = getBufferedReader(logFilePath);
            String line;
            while ((line = reader.readLine()) != null) {
                NginxLogEntity logEntry = parseLogLine(line, fromDate, toDate);
                if (logEntry != null) {
                    System.out.println(logEntry);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
