package ru.yandex.practicum.sleeptracker.analytics;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MaxDurationSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Максимальное продолжительность сессии";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxSession = Optional.ofNullable(sessions).stream().flatMap(List::stream)
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .max().orElse(0L);

        return new SleepAnalysisResult(TITLE, maxSession);
    }
}