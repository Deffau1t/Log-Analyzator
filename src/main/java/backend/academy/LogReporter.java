package backend.academy;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LogReporter {
    void generateReport(String logFilePath, Date fromDate, Date toDate,
        int requestCount, long bodyBytesSentCount, List<Long> bodyBytesSentList,
        Map<String, Integer> resourceCount, Map<Integer, Integer> statusCount);
}
