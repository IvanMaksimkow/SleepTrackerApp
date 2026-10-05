import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadQualitySessionsCountTest extends TestSessions {
    private final BadQualitySessionsCount function = new BadQualitySessionsCount();

    @Test
    void countsOnlyBadSessions() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:15", "02.10.25 08:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 08:00", SleepQuality.BAD),
                session("03.10.25 23:30", "04.10.25 06:20", SleepQuality.BAD),
                session("04.10.25 23:30", "05.10.25 06:20", SleepQuality.NORMAL));
        assertEquals(2L, function.apply(sessions).getValue());
    }

    @Test
    void returnsZeroWhenNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:15", "02.10.25 08:00", SleepQuality.GOOD));
        assertEquals(0L, function.apply(sessions).getValue());
    }
}
