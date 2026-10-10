package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {
    SleepTrackerApp tracker = new SleepTrackerApp();

    @Test
    void shouldParseValidLineCorrectly() {

        String line = "01.10.25 23:25; 02.10.25 07:30;GOOD";

        Optional<SleepingSession> test = tracker.parseLine(line);

        assertTrue(test.isPresent(), "Сессия прошла");
        SleepingSession session = test.get();

        assertEquals(LocalDateTime.of(2025, 10, 1, 23, 25), session.start());
        assertEquals(LocalDateTime.of(2025, 10, 2, 7, 30), session.end());
        assertEquals(SleepQuality.GOOD, session.quality());
    }

    @Test
    void shouldAnalyzeSessionsCorrectly() {

        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 30),
                SleepQuality.GOOD);

        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 22, 30),
                LocalDateTime.of(2025, 10, 3, 6, 30),
                SleepQuality.NORMAL);
        List<SleepingSession> sessions = List.of(session1, session2);

        List<SleepAnalysisResult> result = tracker.analyzeSessions(sessions);
        assertNotNull(result, "Не должно быть null");
        assertFalse(result.isEmpty(), "Не должен быть пустым");
    }
}