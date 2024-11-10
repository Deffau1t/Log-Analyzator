package backend.academy.logConversion;

import java.util.Map;

public class LogAnalysis {
    public static void updateResourceCount(Map<String, Integer> resourceCount, String request) {
        String resource = request.split(" ")[1];
        resourceCount.put(resource, resourceCount.getOrDefault(resource, 0) + 1);
    }

    public static void updateStatusCount(Map<Integer, Integer> statusCount, int status) {
        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
    }

    public static String getStatusName(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 301 -> "Moved Permanently";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
