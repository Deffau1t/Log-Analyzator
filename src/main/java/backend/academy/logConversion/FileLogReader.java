package backend.academy.logConversion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2 @UtilityClass
public class FileLogReader {

    // Обработка входного файла или URL
    public static BufferedReader getBufferedReader(String filePath) throws IOException {
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) { // Исправлено: добавлен оператор ||
            URL url = new URL(filePath);
            return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } else {
            // Проверяем, является ли путь glob-pattern
            if (isGlobPattern(filePath)) {
                // Находим все файлы, соответствующие шаблону
                List<Path> matchingFiles = resolveFiles(filePath);
                if (matchingFiles.isEmpty()) {
                    throw new FileNotFoundException("No file matching the glob pattern was found: " + filePath);
                }
                // Возвращаем BufferedReader для первого найденного файла
                return new BufferedReader(new InputStreamReader(
                    new FileInputStream(matchingFiles.get(0).toFile()), StandardCharsets.UTF_8));
            } else {
                return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            }
        }
    }

    // Проверка на glob
    private static boolean isGlobPattern(String filePath) {
        return filePath.contains("*") || filePath.contains("?") || filePath.contains("[") || filePath.contains("{");
    }

    private List<Path> resolveFiles(String pathPattern) throws IOException {
        // Проверка наличия подстановочных символов
        boolean hasWildcard = pathPattern.contains("*");

        // Если нет подстановочных символов, проверяем существование файла
        if (!hasWildcard) {
            Path filePath = Paths.get(pathPattern);
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                return List.of(filePath); // Вернуть список с одним файлом
            } else {
                return Collections.emptyList(); // Пустой список, если файл не найден
            }
        }

        // Разделяем путь на базовый путь и шаблон glob
        int lastSeparatorIndex = pathPattern.lastIndexOf('\\');
        String basePath = lastSeparatorIndex != -1 ? pathPattern.substring(0, lastSeparatorIndex) : ".";
        String globPattern = lastSeparatorIndex != -1 ? pathPattern.substring(lastSeparatorIndex + 1) : pathPattern;

        // Если присутствуют подстановочные символы, выполняем поиск по шаблону
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        Path rootDir = Paths.get(basePath);

        try (Stream<Path> paths = Files.walk(rootDir)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(matcher::matches)
                .collect(Collectors.toList());
        }
    }
}
