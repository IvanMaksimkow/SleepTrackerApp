
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

final class NightSleep {
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private NightSleep() {
    }

    static Stream<LocalDate> coveredNights(SleepingSession session) {
        return session.getStart().toLocalDate()
                .datesUntil(session.getEnd().toLocalDate().plusDays(1))
                .filter(date -> session.getStart().isBefore(date.atTime(NIGHT_END))
                        && session.getEnd().isAfter(date.atStartOfDay()));
    }

    static boolean isNightSession(SleepingSession session) {
        return coveredNights(session).findAny().isPresent();
    }
}
