package backend.academy.logConversion;

import backend.academy.entities.NginxLogEntity;
import java.util.regex.Pattern;

/**
 * Работа с вариативностью полей
 */
@SuppressWarnings("MagicNumber")
public class LogFieldHandler {
    private LogFieldHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getStatusName(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 206 -> "Partial Content";
            case 301 -> "Moved Permanently";
            case 304 -> "Not Modified";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 416 -> "Requested Range Not Satisfiable";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
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
