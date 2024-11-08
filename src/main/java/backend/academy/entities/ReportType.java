package backend.academy.entities;

import lombok.Getter;

@Getter
public enum ReportType {
    MARKDOWN(1),
    ADOC(2);

    private final int value;

    ReportType(int value) {
        this.value = value;
    }

    public static ReportType fromValue(int value) {
        for (ReportType type : ReportType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Недопустимый тип отчета: " + value);
    }
}
