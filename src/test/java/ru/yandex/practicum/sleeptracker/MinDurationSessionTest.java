package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinDurationSessionTest {
    MinDurationSession min = new MinDurationSession();

    @Test
    void shouldReturnCorrectMinDurationWhenListHasElements() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 4, 0),
                        SleepQuality.BAD));

        SleepAnalysisResult result = min.apply(sessions);

        assertNotNull(result, "Не должен быть null");
        assertEquals(300L, result.getResult());
    }

    @Test
    void shouldReturnZeroWhenIsEmpty() {
        List<SleepingSession> sessions = Collections.emptyList();

        SleepAnalysisResult result = min.apply(sessions);

        assertNotNull(result);
        assertEquals(0L, result.getResult(), "Должно быть ноль");
    }
}