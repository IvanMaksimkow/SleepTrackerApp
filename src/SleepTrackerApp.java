
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> FUNCTIONS = List.of(
            new TotalSessionsCount(),
            new MinSessionDuration(),
            new MaxSessionDuration(),
            new AverageSessionDuration(),
            new BadQualitySessionsCount(),
            new InsomniaNightsCount(),
            new ChronotypeFunction()
    );

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Укажите путь к файлу с логом сна первым аргументом");
            return;
        }
        try {
            List<SleepingSession> sessions = SleepLogReader.read(Path.of(args[0]));
            FUNCTIONS.forEach(function -> {
                SleepAnalysisResult result = function.apply(sessions);
                System.out.println(result.getDescription() + ": " + result.getValue());
            });
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Некорректный формат файла: " + e.getMessage());
        }
    }
}
