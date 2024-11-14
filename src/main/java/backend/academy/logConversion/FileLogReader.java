package backend.academy.logConversion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileLogReader {
    private FileLogReader() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Обработка входного файла или URL
    public static BufferedReader getBufferedReader(String filePath) throws IOException {
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            URL url = new URL(filePath);
            return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } else {
            return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
        }
    }

    // Обработка логов по указанному формату
    public static Matcher splitLogLine(String logLine) {
        String regex = "(\\S+) - (\\S+) \\[(.*?)] \"(.*?)\" (\\d+) (\\d+) \"(.*?)\" \"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(logLine);
    }
}
