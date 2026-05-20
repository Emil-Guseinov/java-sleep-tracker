package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SleeplessNightTest {
    SleeplessNight night = new SleeplessNight();

    @Test
    void shouldReturnZeroSleeplessNightsWhenUserSleepsEveryNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0),
                        SleepQuality.GOOD));

        SleepAnalysisResult result = night.apply(sessions);

        assertNotNull(result);
        assertEquals(0L, result.getResult(), "Без сонных ночей не должно быть ");
    }

    @Test
    void shouldCountSleeplessNightWhenSleepIsOnlyInDaytime() {
        List<SleepingSession> sessions = List.of(new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 16, 0),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 8, 0),
                        LocalDateTime.of(2025, 10, 2, 10, 0),
                        SleepQuality.GOOD));

        SleepAnalysisResult result = night.apply(sessions);

        assertNotNull(result);
        assertEquals(1L, result.getResult(), "Должна быть одна бессонная ночь");
    }

    @Test
    void shouldCorrectlyHandleMonthTransition() {
        List<SleepingSession> sessions = List.of(new SleepingSession(LocalDateTime.of(2025, 10, 31, 14, 0),
                        LocalDateTime.of(2025, 10, 31, 16, 0),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 11, 1, 8, 0),
                        LocalDateTime.of(2025, 11, 1, 10, 0),
                        SleepQuality.GOOD));

        SleepAnalysisResult result = night.apply(sessions);

        assertNotNull(result);
        assertEquals(1L, result.getResult());

    }

    @Test
    void shouldReturnWhenListIsEmptyOrNull() {
        SleepAnalysisResult isEmpty = night.apply(Collections.emptyList());
        SleepAnalysisResult isnull = night.apply(null);

        assertNotNull(isEmpty);
        assertNotNull(isnull);
        assertEquals(0L, isEmpty.getResult(), "Список должен быть пуст 0");
        assertEquals(0L, isnull.getResult(), "Должно быть 0");
    }

}