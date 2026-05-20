package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество сессий сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSession) {
        Integer quantity = sleepingSession.size();
        return new SleepAnalysisResult(TITLE, quantity);
    }
}
