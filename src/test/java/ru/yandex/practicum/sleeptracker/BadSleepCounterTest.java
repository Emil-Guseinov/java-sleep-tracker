package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BadSleepCounterTest {
    BadSleepCounter badSleep = new BadSleepCounter();

    @Test
    void shouldReturnCorrectQualityWhenListHasElement() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 40),
                        LocalDateTime.of(2025, 10, 4, 8, 0),
                        SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 11, 23, 10),
                        LocalDateTime.of(2025, 10, 12, 7, 0),
                        SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 15),
                        LocalDateTime.of(2025, 10, 2, 7, 30),
                        SleepQuality.GOOD));

        SleepAnalysisResult result = badSleep.apply(sessions);

        assertNotNull(result, "Не должен быть null");
        assertEquals(2L, result.getResult(), "Должно быть 2");
    }

    @Test
    void shouldReturnZeroWhenNoBadSession() {
        List<SleepingSession> goodSessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD));

        SleepAnalysisResult result = badSleep.apply(goodSessions);

        assertNotNull(result);
        assertEquals(0L, result.getResult(), "Должно быть ноль плохих снов");
    }

    @Test
    void shouldReturnZeroWhenIsEmpty() {
        List<SleepingSession> sessionsIsEmpty = Collections.emptyList();

        SleepAnalysisResult result = badSleep.apply(sessionsIsEmpty);

        assertNotNull(result);

        assertEquals(0L, result.getResult(), "Должно быть 0");
    }
}