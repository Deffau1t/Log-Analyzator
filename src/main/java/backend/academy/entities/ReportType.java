package backend.academy.entities;

import lombok.Getter;
import java.util.Objects;

@Getter
public enum ReportType {
    MARKDOWN("markdown"),
    ADOC("adoc");

    private final String value;

    ReportType(String  value) {
        this.value = value;
    }

    public static ReportType fromValue(String value) {
        for (ReportType type : ReportType.values()) {
            if (Objects.equals(type.value, value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Недопустимый тип отчета: " + value);
    }
}
