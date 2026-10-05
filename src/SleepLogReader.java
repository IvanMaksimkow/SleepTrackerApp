import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SleepLogReader {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> read(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(SleepLogReader::parseLine)
                .collect(Collectors.toList());
    }

    static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        return new SleepingSession(
                LocalDateTime.parse(parts[0].trim(), FORMATTER),
                LocalDateTime.parse(parts[1].trim(), FORMATTER),
                SleepQuality.valueOf(parts[2].trim()));
    }
}
