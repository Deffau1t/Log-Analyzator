package backend.academy.logConversion;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class LogHandler {
    public static CommandLine optionHandler(String [] args) {

        Options options = new Options();

        // Добавляем опцию для пути к логам
        Option pathOption = Option.builder("p")
            .longOpt("path")
            .desc("Path to log files")
            .hasArg()
            .argName("path")
            .required()
            .build();
        options.addOption(pathOption);

        // Добавляем опцию для даты начала
        Option fromOption = Option.builder("f")
            .longOpt("from")
            .desc("Start date")
            .hasArg()
            .argName("date")
            .build();
        options.addOption(fromOption);

        // Добавляем опцию для конечной даты
        Option toOption = Option.builder("t")
            .longOpt("to")
            .desc("End date")
            .hasArg()
            .argName("date")
            .build();
        options.addOption(toOption);

        // Добавляем опцию для формата вывода
        Option formatOption = Option.builder("o")
            .longOpt("format")
            .desc("Output format")
            .hasArg()
            .argName("format")
            .build();
        options.addOption(formatOption);

        // Создаем объект CommandLineParser
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            // Пытаемся разобрать аргументы командной строки
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            // Если произошла ошибка, выводим сообщение и помощь
            System.out.println(e.getMessage());
            formatter.printHelp("analyzer", options);
            System.exit(1);
            return null;
        }
    }
}
