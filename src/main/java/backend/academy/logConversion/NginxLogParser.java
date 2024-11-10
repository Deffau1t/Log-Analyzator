package backend.academy.logConversion;

import backend.academy.entities.LogFields;
import backend.academy.entities.NginxLogEntity;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import static backend.academy.logConversion.FileLogReader.splitLogLine;

@Log4j2
public class NginxLogParser {
    public static NginxLogEntity parseLogLine(String logLine, Date fromDate, Date toDate) {
        Matcher matcher = splitLogLine(logLine);

        NginxLogEntity logEntry = new NginxLogEntity();
        if (matcher.find()) {
            logEntry.remoteAddress(matcher.group(LogFields.REMOTE_ADDRESS.index()));
            logEntry.remoteUser(matcher.group(LogFields.REMOTE_USER.index()));

            if (fromDate != null || toDate != null) {
                Date filteredLogDate = logDateFilter(fromDate, toDate, matcher);
                if (filteredLogDate != null) {
                    logEntry.timeLocal(filteredLogDate);
                } else {
                    return null;
                }
            } else {
                logEntry.timeLocal(parseLogDate(matcher.group(LogFields.TIME_LOCAL.index())));
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
        Date logDate = parseLogDate(matcher.group(LogFields.TIME_LOCAL.index()));
        if (fromDate != null) {
            if (toDate != null) {
                if (logDate.before(fromDate) || logDate.after(toDate)) {
                    return null;
                } else {
                    return logDate;
                }
            } else {
                if (logDate.before(fromDate)) {
                    return null;
                } else {
                    return logDate;
                }
            }
        } else {
            if (logDate.after(toDate)) {
                return null;
            } else {
                return logDate;
            }
        }
    }

    public static Date parseLogDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Date parseRangeDate(String dateString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return inputDateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error("Invalid ISO 8601 date format");
            throw new IllegalArgumentException("Invalid ISO 8601 date format", e);
        }
    }
}
