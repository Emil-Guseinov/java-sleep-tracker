package ru.yandex.practicum.sleeptracker.analytics;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNight implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long sleeplessNightsCount = Optional.ofNullable(sessions)
                .filter(list -> !list.isEmpty()) // Если список пустой, цепочка прервется и уйдет в orElse(0L)
                .map(list -> {

                    LocalDate startDate = list.stream()
                            .map(SleepingSession::start)
                            .min(LocalDateTime::compareTo)
                            .map(date -> date.toLocalTime().isAfter(LocalTime.NOON)
                                    ? date.toLocalDate().plusDays(1) : date.toLocalDate())
                            .orElseThrow();

                    LocalDate endDate = list.stream()
                            .map(SleepingSession::end)
                            .max(LocalDateTime::compareTo).map(date -> date.toLocalTime().isAfter(LocalTime.NOON)
                                    ? date.toLocalDate().plusDays(1) : date.toLocalDate()).orElseThrow();

                    return Stream.iterate(startDate, date -> date.plusDays(1))
                            .takeWhile(date -> !date.isAfter(endDate))
                            .filter(nightDate -> {
                                LocalDateTime nightStart = nightDate.atStartOfDay();
                                LocalDateTime nightEnd = nightDate.atTime(6, 0);
                                boolean userSleep = list.stream()
                                        .anyMatch(session -> session.start().isBefore(nightEnd) && session.end().isAfter(nightStart));
                                return !userSleep;
                            }).count();
                }).orElse(0L);
        return new SleepAnalysisResult(TITLE, sleeplessNightsCount);
    }
}
