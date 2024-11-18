package backend.academy.logConversion;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
@UtilityClass
public class LogHandler {
    public static CommandLine optionHandler(String[] args) {
        Options options = new Options();

        // Добавляем опцию для пути к логам
        Option pathOption = Option.builder()
            .longOpt("path")
            .desc("Path to log files")
            .hasArg()
            .required()
            .build();
        options.addOption(pathOption);

        // Добавляем опцию для даты начала
        Option fromOption = Option.builder()
            .longOpt("from")
            .desc("Start date")
            .hasArg()
            .build();
        options.addOption(fromOption);

        // Добавляем опцию для конечной даты
        Option toOption = Option.builder()
            .longOpt("to")
            .desc("End date")
            .hasArg()
            .build();
        options.addOption(toOption);

        // Добавляем опцию для формата вывода
        Option formatOption = Option.builder()
            .longOpt("format")
            .desc("Output format")
            .hasArg()
            .build();
        options.addOption(formatOption);

        // Добавляем опцию для фильтрации по полю
        Option filterFieldOption = Option.builder()
            .longOpt("filter-field")
            .desc("Output field filter")
            .hasArg()
            .build();
        options.addOption(filterFieldOption);

        // Добавляем опцию для фильтрации по значению
        Option filterValueOption = Option.builder()
            .longOpt("filter-value")
            .desc("Output value filter")
            .hasArg()
            .build();
        options.addOption(filterValueOption);

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
