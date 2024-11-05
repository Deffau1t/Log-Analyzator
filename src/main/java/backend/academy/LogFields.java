package backend.academy;

import lombok.Getter;

@Getter
public enum LogFields {
    REMOTE_ADDRESS(0),
    REMOTE_USER(1),
    TIME_LOCAL(2),
    REQUEST(3),
    STATUS(4),
    BODY_BYTES_SENT(5),
    HTTP_REFERER(6),
    HTTP_USER_AGENT(7);

    private final int index;

    LogFields(int index) {
        this.index = index;
    }
}
