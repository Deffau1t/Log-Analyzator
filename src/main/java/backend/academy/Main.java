package backend.academy;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import static backend.academy.Analyzator.logAnalize;

@UtilityClass
@Log4j2
public class Main {
    public static void main(String[] args) {
        log.info("Analyzing logs is started.");
        logAnalize(args);
    }
}
