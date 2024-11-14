package backend.academy.entities;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Сущность nginx логов
 */
@Getter
@Setter
@ToString
public class NginxLogEntity {
    private String remoteAddress;
    private String remoteUser;
    private Date timeLocal;
    private String request;
    private int status;
    private long bodyBytesSent;
    private String httpReferer;
    private String httpUserAgent;
}
