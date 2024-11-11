package backend.academy.logConversion;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
public class LogHandler {
    private LogHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CommandLine optionHandler(String[] args) {
        Options options = new Options();

        // Добавляем опцию для пути к логам
        Option pathOption = Option.builder("p")
            .longOpt("path")
            .desc("Path to log files")
            .hasArg()
            .required()
            .build();
        options.addOption(pathOption);

        // Добавляем опцию для даты начала
        Option fromOption = Option.builder("f")
            .longOpt("from")
            .desc("Start date")
            .hasArg()
            .build();
        options.addOption(fromOption);

        // Добавляем опцию для конечной даты
        Option toOption = Option.builder("t")
            .longOpt("to")
            .desc("End date")
            .hasArg()
            .build();
        options.addOption(toOption);

        // Добавляем опцию для формата вывода
        Option formatOption = Option.builder("o")
            .longOpt("format")
            .desc("Output format")
            .hasArg()
            .build();
        options.addOption(formatOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            // Разбор аргументов командной строки
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            log.error(e.getMessage());
            formatter.printHelp("analyzer", options);
            System.exit(1);
            return null;
        }
    }
}
