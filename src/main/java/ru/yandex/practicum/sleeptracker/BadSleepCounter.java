package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BadSleepCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество сессий с плохим сном";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badSleepCounter = Optional.ofNullable(sessions).stream()
                .flatMap(List::stream).filter(session -> session.quality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult(TITLE, badSleepCounter);
    }
}
