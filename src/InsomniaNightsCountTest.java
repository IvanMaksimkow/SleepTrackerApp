import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsomniaNightsCountTest extends TestSessions {
    private final InsomniaNightsCount function = new InsomniaNightsCount();

    private Object count(SleepingSession... sessions) {
        return function.apply(List.of(sessions)).getValue();
    }

    @Test
    void emptyLogHasNoInsomniaNights() {
        assertEquals(0L, function.apply(List.of()).getValue());
    }

    @Test
    void sleepCrossingMidnightIsNotInsomnia() {
        assertEquals(0L, count(session("01.10.25 23:00", "02.10.25 08:00")));
    }

    @Test
    void sleepEndingBeforeSixIsNotInsomnia() {
        assertEquals(0L, count(session("01.10.25 19:00", "02.10.25 05:00")));
    }

    @Test
    void sleepStartingAfterMidnightIsNotInsomnia() {
        assertEquals(0L, count(
                session("02.10.25 01:00", "02.10.25 10:00")));
    }

    @Test
    void sleepInsideNightIsNotInsomnia() {
        assertEquals(0L, count(session("02.10.25 02:00", "02.10.25 05:00")));
    }

    @Test
    void daytimeSleepOnlyIsInsomnia() {
        assertEquals(1L, count(session("02.10.25 07:00", "02.10.25 11:00")));
    }

    @Test
    void sleepStartingExactlyAtSixDoesNotCoverNight() {
        assertEquals(1L, count(session("02.10.25 06:00", "02.10.25 10:00")));
    }

    @Test
    void sleepEndingExactlyAtMidnightDoesNotCoverNight() {
        assertEquals(1L, count(session("01.10.25 18:00", "02.10.25 00:00")));
    }

    @Test
    void firstSessionAfterNoonUsesNextNight() {
        assertEquals(0L, count(
                session("03.10.25 14:30", "03.10.25 15:20"),
                session("03.10.25 23:30", "04.10.25 06:20")));
    }

    @Test
    void firstSessionBeforeNoonUsesPreviousNight() {
        assertEquals(1L, count(
                session("03.10.25 08:00", "03.10.25 10:00"),
                session("03.10.25 23:00", "04.10.25 07:00")));
    }

    @Test
    void missedNightBetweenSessions() {
        assertEquals(1L, count(
                session("01.10.25 22:00", "02.10.25 07:00"),
                session("03.10.25 07:00", "03.10.25 11:00")));
    }

    @Test
    void missedNightAcrossMonthBoundary() {
        assertEquals(1L, count(
                session("29.11.25 23:00", "30.11.25 07:00"),
                session("01.12.25 23:00", "02.12.25 07:00")));
    }

    @Test
    void longSessionCoversSeveralNights() {
        assertEquals(0L, count(session("01.10.25 22:00", "04.10.25 08:00")));
    }

    @Test
    void sleepWithinSingleEveningHasNoNightInLogPeriod() {
        assertEquals(0L, count(session("01.10.25 17:00", "01.10.25 23:00")));
    }
}
