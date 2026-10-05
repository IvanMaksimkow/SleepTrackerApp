import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SessionDurationFunctionsTest extends TestSessions {
    private final List<SleepingSession> sessions = List.of(
            session("01.10.25 22:15", "02.10.25 08:00"),
            session("03.10.25 14:30", "03.10.25 15:20"),
            session("03.10.25 23:30", "04.10.25 06:20"));

    @Test
    void minDuration() {
        assertEquals(50L, new MinSessionDuration().apply(sessions).getValue());
    }

    @Test
    void minDurationOfSingleSession() {
        assertEquals(585L, new MinSessionDuration().apply(sessions.subList(0, 1)).getValue());
    }

    @Test
    void maxDuration() {
        assertEquals(585L, new MaxSessionDuration().apply(sessions).getValue());
    }

    @Test
    void maxDurationOfSingleSession() {
        assertEquals(50L, new MaxSessionDuration().apply(sessions.subList(1, 2)).getValue());
    }

    @Test
    void averageDuration() {
        assertEquals(348.3, new AverageSessionDuration().apply(sessions).getValue());
    }

    @Test
    void averageDurationOfEmptyList() {
        assertEquals(0.0, new AverageSessionDuration().apply(List.of()).getValue());
    }
}
