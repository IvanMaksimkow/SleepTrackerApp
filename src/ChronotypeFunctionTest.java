import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeFunctionTest extends TestSessions {
    private final ChronotypeFunction function = new ChronotypeFunction();

    private Object type(SleepingSession... sessions) {
        return function.apply(List.of(sessions)).getValue();
    }

    @Test
    void owlNight() {
        assertEquals(Chronotype.OWL, type(session("02.10.25 23:30", "03.10.25 10:00")));
    }

    @Test
    void owlNightStartedAfterMidnight() {
        assertEquals(Chronotype.OWL, type(session("03.10.25 01:00", "03.10.25 10:00")));
    }

    @Test
    void larkNight() {
        assertEquals(Chronotype.LARK, type(session("02.10.25 21:00", "03.10.25 06:00")));
    }

    @Test
    void doveNight() {
        assertEquals(Chronotype.DOVE, type(session("02.10.25 22:30", "03.10.25 07:30")));
    }

    @Test
    void borderlineTimesAreDove() {
        assertEquals(Chronotype.DOVE, type(
                session("02.10.25 23:00", "03.10.25 09:00"),
                session("03.10.25 22:00", "04.10.25 07:00")));
    }

    @Test
    void mostFrequentTypeWins() {
        assertEquals(Chronotype.LARK, type(
                session("01.10.25 21:00", "02.10.25 06:00"),
                session("02.10.25 21:00", "03.10.25 06:00"),
                session("03.10.25 23:30", "04.10.25 10:00")));
    }

    @Test
    void tieIsDove() {
        assertEquals(Chronotype.DOVE, type(
                session("01.10.25 21:00", "02.10.25 06:00"),
                session("02.10.25 23:30", "03.10.25 10:00")));
    }

    @Test
    void daytimeSessionsAreIgnored() {
        assertEquals(Chronotype.LARK, type(
                session("01.10.25 21:00", "02.10.25 06:00"),
                session("02.10.25 14:30", "02.10.25 15:20"),
                session("03.10.25 14:30", "03.10.25 15:20")));
    }

    @Test
    void emptyLogIsDove() {
        assertEquals(Chronotype.DOVE, function.apply(List.of()).getValue());
    }
}
