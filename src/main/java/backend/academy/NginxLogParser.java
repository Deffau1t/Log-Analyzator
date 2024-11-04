package backend.academy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NginxLogParser {

    public static NginxLogEntity parseLogLine(String logLine, Date startDate, Date endDate) {
        String[] logParts = logLine.split("\"");

        NginxLogEntity logEntry = new NginxLogEntity();
        logEntry.remoteAddress(logParts[LogFields.REMOTE_ADDRESS.index()].trim());
        logEntry.remoteUser(logParts[LogFields.REMOTE_USER.index()].trim());

        Date filteredLogDate = logDateFilter(startDate, endDate, logParts);
        if (filteredLogDate != null) {
            logEntry.timeLocal(filteredLogDate);
        } else {
            return null;
        }

        logEntry.request(logParts[LogFields.REQUEST.index()].trim());
        logEntry.status(Integer.parseInt(logParts[LogFields.STATUS.index()].trim()));
        logEntry.bodyBytesSent(Long.parseLong(logParts[LogFields.BODY_BYTES_SENT.index()].trim()));
        logEntry.httpReferer(logParts[LogFields.HTTP_REFERER.index()].trim());
        logEntry.httpUserAgent(logParts[LogFields.HTTP_USER_AGENT.index()].trim());

        return logEntry;
    }

    private static Date logDateFilter(Date startDate, Date endDate, String[] logParts) {
        Date logDate = parseDate(logParts[LogFields.TIME_LOCAL.index()].trim());
        if (logDate.before(startDate) || logDate.after(endDate)) {
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
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}
