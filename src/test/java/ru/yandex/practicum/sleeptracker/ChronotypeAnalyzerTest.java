package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analytics.ChronotypeAnalyzer;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeAnalyzerTest {
    ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

    @Test
    void shouldReturnOwlWhenOwlNightsPrevail() {
        SleepingSession owlNight =
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 1, 23, 30),
                        LocalDateTime.of(2026, 5, 2, 9, 30),
                        SleepQuality.GOOD);
        SleepAnalysisResult result = analyzer.apply(List.of(owlNight));
        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        assertEquals(Chronotype.OWL, result.getResult());
    }

    @Test
    void shouldReturnLarkWhenLarkNightsPrevail() {

        SleepingSession larkNight =
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 1, 21, 30),
                        LocalDateTime.of(2026, 5, 2, 6, 30),
                        SleepQuality.GOOD);

        SleepAnalysisResult result = analyzer.apply(List.of(larkNight));

        assertEquals(Chronotype.LARK, result.getResult());
    }

    @Test
    void shouldReturnPigeonWhenTieOccurs() {
        SleepingSession owlNight = new SleepingSession(
                LocalDateTime.of(2026, 5, 1, 23, 30),
                LocalDateTime.of(2026, 5, 3, 6, 30),
                SleepQuality.GOOD);

        SleepingSession larkNight = new SleepingSession(
                LocalDateTime.of(2026, 5, 2, 21, 30),
                LocalDateTime.of(2026, 5, 3, 6, 30),
                SleepQuality.GOOD);

        SleepAnalysisResult result = analyzer.apply(List.of(owlNight, larkNight));

        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    void shouldIgnoreBadSleepAndDaySessions() {
        SleepingSession badNight = new SleepingSession(
                LocalDateTime.of(2026, 5, 1, 23, 30),
                LocalDateTime.of(2026, 5, 2, 9, 30),
                SleepQuality.BAD);

        SleepingSession daySleep = new SleepingSession(
                LocalDateTime.of(2026, 5, 2, 14, 0),
                LocalDateTime.of(2026, 5, 2, 16, 0),
                SleepQuality.GOOD);

        SleepingSession larkNight = new SleepingSession(
                LocalDateTime.of(2026, 5, 3, 21, 30),
                LocalDateTime.of(2026, 5, 4, 6, 30),
                SleepQuality.GOOD);

        SleepAnalysisResult result = analyzer.apply(List.of(badNight, daySleep, larkNight));

        assertEquals(Chronotype.LARK, result.getResult());
    }

    @Test
    void shouldReturnPigeonWhenNoValidNights() {
        SleepAnalysisResult s = analyzer.apply(Collections.emptyList());
        assertEquals(Chronotype.PIGEON, s.getResult());

    }

}