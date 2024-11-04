package backend.academy;

import lombok.Getter;

@Getter
public enum LogFields {
    REMOTE_ADDRESS(1),
    REMOTE_USER(3),
    TIME_LOCAL(5),
    REQUEST(7),
    STATUS(9),
    BODY_BYTES_SENT(11),
    HTTP_REFERER(13),
    HTTP_USER_AGENT(15);

    private final int index;

    LogFields(int index) {
        this.index = index;
    }
}
