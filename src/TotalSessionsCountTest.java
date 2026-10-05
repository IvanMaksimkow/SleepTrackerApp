import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSessionsCountTest extends TestSessions {
    private final TotalSessionsCount function = new TotalSessionsCount();

    @Test
    void countsAllSessions() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:15", "02.10.25 08:00"),
                session("02.10.25 23:00", "03.10.25 08:00"),
                session("03.10.25 14:30", "03.10.25 15:20"));
        assertEquals(3, function.apply(sessions).getValue());
    }

    @Test
    void returnsZeroForEmptyList() {
        assertEquals(0, function.apply(List.of()).getValue());
    }
}
