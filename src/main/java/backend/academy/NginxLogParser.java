package backend.academy;

import backend.academy.entities.LogFields;
import backend.academy.entities.NginxLogEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import static backend.academy.FileLogReader.splitLogLine;

public class NginxLogParser {

    public static NginxLogEntity parseLogLine(String logLine, Date fromDate, Date toDate) {
        Matcher matcher = splitLogLine(logLine);

        NginxLogEntity logEntry = new NginxLogEntity();
        if (matcher.find()) {
            logEntry.remoteAddress(matcher.group(LogFields.REMOTE_ADDRESS.index()));
            logEntry.remoteUser(matcher.group(LogFields.REMOTE_USER.index()));

            Date filteredLogDate = logDateFilter(fromDate, toDate, matcher);
            if (filteredLogDate != null) {
                logEntry.timeLocal(filteredLogDate);
            } else {
                return null;
            }

            logEntry.request(matcher.group(LogFields.REQUEST.index()));
            logEntry.status(Integer.parseInt(matcher.group(LogFields.STATUS.index())));
            logEntry.bodyBytesSent(Long.parseLong(matcher.group(LogFields.BODY_BYTES_SENT.index())));
            logEntry.httpReferer(matcher.group(LogFields.HTTP_REFERER.index()));
            logEntry.httpUserAgent(matcher.group(LogFields.HTTP_USER_AGENT.index()));
        }

        return logEntry;
    }

    private static Date logDateFilter(Date fromDate, Date toDate, Matcher matcher) {
        Date logDate = parseDate(matcher.group(LogFields.TIME_LOCAL.index()));
        if (logDate.before(fromDate) || logDate.after(toDate)) {
            return null;
        } else {
            return logDate;
        }
    }

    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid ISO 8601 date format", e);
        }
    }
}
