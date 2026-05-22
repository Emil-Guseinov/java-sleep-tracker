package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analytics.SessionCounter;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionCounterTest {

    @Test
    void shouldReturnCorrectQuantityWhenListHasElements() {
        SessionCounter s = new SessionCounter();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 6, 23, 30),
                        LocalDateTime.of(2025, 10, 7, 5, 50),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 8, 23, 50),
                        LocalDateTime.of(2025, 10, 9, 7, 10),
                        SleepQuality.GOOD));

        SleepAnalysisResult result = s.apply(sessions);

        assertNotNull(result);
        assertEquals(2, result.getResult());
    }

    @Test
    void shouldReturnZeroQuantityWhenListIsEmpty() {
        SessionCounter s = new SessionCounter();
        List<SleepingSession> emptyList = Collections.emptyList();

        SleepAnalysisResult result = s.apply(emptyList);

        assertEquals(0, result.getResult());


    }
}