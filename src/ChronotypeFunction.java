
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final int MINUTES_IN_DAY = 24 * 60;
    private static final int NOON_OFFSET = 12 * 60;
    private static final int OWL_MIN_BEDTIME = 23 * 60 - NOON_OFFSET;
    private static final int LARK_MAX_BEDTIME = 22 * 60 - NOON_OFFSET;
    private static final LocalTime OWL_MIN_WAKE = LocalTime.of(9, 0);
    private static final LocalTime LARK_MAX_WAKE = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Хронотип пользователя", detect(sessions));
    }

    private Chronotype detect(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(NightSleep::isNightSession)
                .collect(Collectors.groupingBy(this::classify, Collectors.counting()));
        long owls = counts.getOrDefault(Chronotype.OWL, 0L);
        long larks = counts.getOrDefault(Chronotype.LARK, 0L);
        long doves = counts.getOrDefault(Chronotype.DOVE, 0L);
        if (owls > larks && owls > doves) {
            return Chronotype.OWL;
        }
        if (larks > owls && larks > doves) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private Chronotype classify(SleepingSession session) {
        int bedtime = bedtimeOffset(session.getStart().toLocalTime());
        LocalTime wake = session.getEnd().toLocalTime();
        if (bedtime > OWL_MIN_BEDTIME && wake.isAfter(OWL_MIN_WAKE)) {
            return Chronotype.OWL;
        }
        if (bedtime < LARK_MAX_BEDTIME && wake.isBefore(LARK_MAX_WAKE)) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private int bedtimeOffset(LocalTime time) {
        int minutes = time.getHour() * 60 + time.getMinute();
        return (minutes - NOON_OFFSET + MINUTES_IN_DAY) % MINUTES_IN_DAY;
    }
}
