package ru.yandex.practicum.sleeptracker.analytics;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AverageDurationSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Средняя продолжительность сессии";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double averageMinutesSession = Optional.ofNullable(sessions).stream().flatMap(List::stream)
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .average().orElse(0.0);
        long result = Math.round(averageMinutesSession);
        return new SleepAnalysisResult(TITLE, result);
    }

}
