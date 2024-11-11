package backend.academy.reportGenerating;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ParameterNumber")
public interface LogReporter {
    void generateReport(String logFilePath, String fromDate, String toDate,
        int requestCount, long bodyBytesSentCount, List<Long> bodyBytesSentList,
        Map<String, Integer> resourceCount, Map<Integer, Integer> statusCount, Map<String, Integer> requestType,
        Map<String, Integer> addressCount);
}
