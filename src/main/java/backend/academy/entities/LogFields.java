package backend.academy.entities;

import lombok.Getter;

@Getter
public enum LogFields {
    REMOTE_ADDRESS(1),
    REMOTE_USER(2),
    TIME_LOCAL(3),
    REQUEST(4),
    STATUS(5),
    BODY_BYTES_SENT(6),
    HTTP_REFERER(7),
    HTTP_USER_AGENT(8);

    private final int index;

    LogFields(int index) {
        this.index = index;
    }
}
