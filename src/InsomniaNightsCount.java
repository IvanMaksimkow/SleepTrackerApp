
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InsomniaNightsCount implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Количество бессонных ночей", count(sessions));
    }

    private long count(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }
        SleepingSession first = sessions.get(0);
        SleepingSession last = sessions.get(sessions.size() - 1);
        LocalDate firstNight = first.getStart().toLocalTime().isBefore(LocalTime.NOON)
                ? first.getStart().toLocalDate()
                : first.getStart().toLocalDate().plusDays(1);
        LocalDate lastNight = last.getEnd().toLocalDate();

        long totalNights = Math.max(0, ChronoUnit.DAYS.between(firstNight, lastNight) + 1);
        Set<LocalDate> sleptNights = sessions.stream()
                .flatMap(NightSleep::coveredNights)
                .filter(night -> !night.isBefore(firstNight) && !night.isAfter(lastNight))
                .collect(Collectors.toSet());
        return totalNights - sleptNights.size();
    }
}
