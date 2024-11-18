package backend.academy.logConversion;

import backend.academy.entities.HttpStatus;
import backend.academy.entities.NginxLogEntity;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * Работа с вариативностью полей
 */
@SuppressWarnings("MagicNumber")
@UtilityClass
public class LogFieldHandler {
    public static String getStatusName(int statusCode) {
        return HttpStatus.fromCode(statusCode).message();
    }

    public static boolean isApproachToFilters(String filterField, String filterValue, NginxLogEntity logEntry) {
        if (filterValue != null) {
            Pattern pattern = Pattern.compile(filterValue);
            if (filterField != null) {
                String logEntryValue = getField(filterField, logEntry);
                if (logEntryValue == null) {
                    return false;
                }
                return pattern.matcher(logEntryValue).find();
            } else {
                return pattern.matcher(logEntry.toString()).find();
            }
        } else {
            return true;
        }
    }

    @SuppressWarnings("ReturnCount")
    private static String getField(String filterField, NginxLogEntity logEntity) {
        switch (filterField) {
            case "remoteAddress" -> {
                return logEntity.remoteAddress();
            }
            case "remoteUser" -> {
                return logEntity.remoteUser();
            }
            case "timeLocal" -> {
                return logEntity.timeLocal().toString();
            }
            case "request" -> {
                return logEntity.request();
            }
            case "status" -> {
                return String.valueOf(logEntity.status());
            }
            case "bodyBytesSent" -> {
                return String.valueOf(logEntity.bodyBytesSent());
            }
            case "httpReferer" -> {
                return logEntity.httpReferer();
            }
            case "httpUserAgent" -> {
                return logEntity.httpUserAgent();
            }
            default -> {
                return null;
            }
        }
    }
}
