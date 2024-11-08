package backend.academy.reportGenerating;

import backend.academy.entities.ReportType;

public class LogReporterFactory {

    private LogReporterFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static LogReporter createReporter(int correctReportChoice) {
        ReportType reportType = ReportType.fromValue(correctReportChoice);
        return switch (reportType) {
            case MARKDOWN -> new LogMarkdownReporter();
            case ADOC -> new LogAdocReporter();
            default -> throw new IllegalArgumentException("Неизвестный тип отчета: " + reportType);
        };
    }
}
