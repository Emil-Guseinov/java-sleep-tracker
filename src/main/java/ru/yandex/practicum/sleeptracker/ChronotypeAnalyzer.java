package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final LocalTime OWL_SLEEP_FROM = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_FROM = LocalTime.of(9, 0);

    private static final LocalTime LARK_SLEEP_TO = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_TO = LocalTime.of(7, 0);


    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        List<Chronotype> nightTypes = sessions.stream()
                .filter(session -> session.quality() != SleepQuality.BAD)
                .filter(this::isNightSleep)
                .map(this::classifyNight)
                .collect(Collectors.toList());

        if (nightTypes.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", Chronotype.PIGEON);
        }
        Map<Chronotype, Long> counts = nightTypes.stream().
                collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Chronotype finalChronotype = determineUserChronotype(counts);
        return new SleepAnalysisResult("Хронотип пользователя", finalChronotype);
    }

    private boolean isNightSleep(SleepingSession session) {
        LocalTime startTime = session.start().toLocalTime();
        return startTime.isBefore(LocalTime.of(6, 0)) || startTime.isAfter(LocalTime.of(18, 0));
    }

    private Chronotype classifyNight(SleepingSession session) {
        LocalTime start = session.start().toLocalTime();
        LocalTime end = session.end().toLocalTime();

        if (start.isAfter(OWL_SLEEP_FROM) && end.isAfter(OWL_WAKE_FROM)) {
            return Chronotype.OWL;

        }
        if (start.isBefore(LARK_SLEEP_TO) && end.isBefore(LARK_WAKE_TO)) {
            return Chronotype.LARK;
        }
        return Chronotype.PIGEON;
    }

    private Chronotype determineUserChronotype(Map<Chronotype, Long> counts) {
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);
        long max = Math.max(pigeonCount, Math.max(owlCount, larkCount));

        if ((owlCount == max && larkCount == max) ||
                (owlCount == max && pigeonCount == max) ||
                (larkCount == max && pigeonCount == max)) {
            return Chronotype.PIGEON;

        }
        if (owlCount == max) return Chronotype.OWL;
        if (larkCount == max) return Chronotype.LARK;
        return Chronotype.PIGEON;
    }
}
