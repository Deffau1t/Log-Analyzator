package backend.academy;

import backend.academy.entities.NginxLogEntity;
import backend.academy.reportGenerating.LogReporter;
import backend.academy.reportGenerating.LogReporterFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import static backend.academy.logConversion.FileLogReader.getBufferedReader;
import static backend.academy.logConversion.LogAnalysis.getRequestType;
import static backend.academy.logConversion.LogAnalysis.updateResourceCount;
import static backend.academy.logConversion.LogAnalysis.updateStatusCount;
import static backend.academy.logConversion.LogFieldHandler.isApproachToFilters;
import static backend.academy.logConversion.LogHandler.optionHandler;
import static backend.academy.logConversion.NginxLogParser.parseLogLine;
import static backend.academy.logConversion.NginxLogParser.parseRangeDate;
import static backend.academy.logConversion.NginxLogParser.reportFormatDate;

@Log4j2
@UtilityClass
public class Analyzator {
    public static void logAnalize(String[] args) {
        CommandLine cmd = optionHandler(args);
        String path = cmd.getOptionValue("path");
        String from = cmd.getOptionValue("from");
        String to = cmd.getOptionValue("to");
        String format = cmd.getOptionValue("format");
        String filterField = cmd.getOptionValue("filter-field");
        String filterValue = cmd.getOptionValue("filter-value");

        // Обработка случая не переданного формата
        format = format == null ? "markdown" : format;

        Date fromDate;
        if (from != null) {
            fromDate = parseRangeDate(from);
        } else {
            fromDate = null;
        }
        Date toDate;
        if (to != null) {
            toDate = parseRangeDate(to);
        } else {
            toDate = null;
        }

        Map<String, Integer> resourceCount = new HashMap<>();
        Map<Integer, Integer> statusCount = new HashMap<>();
        List<Long> bodyBytesSentList = new ArrayList<>();
        Map<String, Integer> requestType = new HashMap<>();
        Map<String, Integer> addressCount = new HashMap<>();
        long bodyBytesSentCount = 0;
        int requestCount = 0;

        try (
            BufferedReader reader = getBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    NginxLogEntity logEntry = parseLogLine(line, fromDate, toDate);
                    if (logEntry != null) {
                        if (!isApproachToFilters(filterField, filterValue, logEntry)) {
                            continue;
                        }
                        updateResourceCount(resourceCount, logEntry.request());
                        updateStatusCount(statusCount, logEntry.status());
                        bodyBytesSentList.add(logEntry.bodyBytesSent());
                        bodyBytesSentCount += logEntry.bodyBytesSent();
                        requestCount++;
                        requestType.put(getRequestType(logEntry.request()),
                            requestType.getOrDefault(getRequestType(logEntry.request()), 0) + 1);
                        addressCount.put(logEntry.remoteAddress(),
                            addressCount.getOrDefault(logEntry.remoteAddress(), 0) + 1);

                    }
                } catch (NullPointerException e) {
                    log.error(e.getMessage());
                }
            }
            log.info("Logs handled successfully.");
        } catch (
            IOException e) {
            log.error(e.getMessage());
            log.info("Logs can't be handled at {}.", path);
        }

        log.info("Generating report...");

        String formattedFromDate = reportFormatDate(fromDate);
        String formattedToDate = reportFormatDate(toDate);

        LogReporter reporter = LogReporterFactory.createReporter(format);

        reporter.generateReport(path, formattedFromDate, formattedToDate,
            requestCount, bodyBytesSentCount, bodyBytesSentList, resourceCount, statusCount, requestType, addressCount);
        log.info("Report is done. Result in reports/{}Report", format);
    }
}
