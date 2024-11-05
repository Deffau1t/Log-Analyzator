package backend.academy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileLogReader {
    public static BufferedReader getBufferedReader(String filePath) throws IOException {
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            URL url = new URL(filePath);
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            return new BufferedReader(new FileReader(filePath));
        }
    }

    public static String[] splitLogLine(String logLine) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(logLine);
        String[] parts = new String[16]; // Ожидаем 16 частей
        int index = 0;

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                parts[index++] = matcher.group(1); // Захваченная группа в кавычках
            } else {
                parts[index++] = matcher.group(2); // Группа без кавычек
            }
        }

        return parts;
    }
}
