package backend.academy.reportGenerating;

import backend.academy.entities.ReportType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogReporterFactory {
    public static LogReporter createReporter(String reportChoice) {
        ReportType reportType = ReportType.fromValue(reportChoice);
        return switch (reportType) {
            case MARKDOWN -> new LogMarkdownReporter();
            case ADOC -> new LogAdocReporter();
            default -> throw new IllegalArgumentException("Неизвестный тип отчета: " + reportType);
        };
    }
}
