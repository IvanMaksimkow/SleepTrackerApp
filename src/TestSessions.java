import java.time.LocalDateTime;

abstract class TestSessions {
    static SleepingSession session(String start, String end) {
        return session(start, end, SleepQuality.NORMAL);
    }

    static SleepingSession session(String start, String end, SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.parse(start, SleepLogReader.FORMATTER),
                LocalDateTime.parse(end, SleepLogReader.FORMATTER),
                quality);
    }
}
