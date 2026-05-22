package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analytics.AverageDurationSession;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AverageDurationSessionTest {
    AverageDurationSession average = new AverageDurationSession();

    @Test
    void shouldReturnCorrectAverageDurationWhenListHasElements() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 4, 0),
                        SleepQuality.BAD));

        SleepAnalysisResult result = average.apply(sessions);

        assertNotNull(result, "Не должен быть null");
        assertEquals(390L, result.getResult());
    }

    @Test
    void shouldReturnZeroWhenIsEmpty() {
        List<SleepingSession> sessions = Collections.emptyList();

        SleepAnalysisResult result = average.apply(sessions);

        assertNotNull(result);
        assertEquals(0L, result.getResult(), "Должно быть ноль");
    }
}