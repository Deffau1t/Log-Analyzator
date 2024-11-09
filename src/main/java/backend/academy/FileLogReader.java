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

    public static Matcher splitLogLine(String logLine) {
        String regex = "(\\S+) - (\\S+) \\[(.*?)] \"(.*?)\" (\\d+) (\\d+) \"(.*?)\" \"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(logLine);
    }
}
