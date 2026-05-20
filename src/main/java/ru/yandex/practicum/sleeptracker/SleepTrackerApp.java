package ru.yandex.practicum.sleeptracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final Path SLEEP_LOG = Paths.get("src/main/resources/sleep_log.txt");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTIC_FUNCTIONS =
            List.of(new SessionCounter(),
                    new MinDurationSession(),
                    new MaxDurationSession(),
                    new AverageDurationSession(),
                    new BadSleepCounter(),
                    new SleeplessNight(),
                    new ChronotypeAnalyzer());

    public static void main(String[] args) {
        SleepTrackerApp tracker = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = tracker.readFile(tracker.getFile());
            List<SleepAnalysisResult> result = tracker.analyzeSessions(sessions);

            result.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла " + e.getMessage());
        }


    }

    List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return ANALYTIC_FUNCTIONS.stream()
                .map(function -> function.apply(sessions))
                .collect(Collectors.toList());
    }

    private File getFile() throws FileNotFoundException {


        File file = SleepTrackerApp.SLEEP_LOG.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("Не существует файл " + file);
        }
        return file;
    }

    private List<SleepingSession> readFile(File file) {

        List<SleepingSession> sessions = new ArrayList<>();

        try (Stream<String> logLines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            sessions = logLines.map(this::parseLine).filter(Optional::isPresent).
                    map(Optional::get).collect(Collectors.toList());

            if (sessions.isEmpty()) {
                System.out.println("Пустой файл " + file.getName());
            }

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла " + e.getMessage());

        }

        return sessions;
    }

    Optional<SleepingSession> parseLine(String line) {
        try {
            String[] lines = line.split(";");
            LocalDateTime start = LocalDateTime.parse(lines[0].trim(), DATE_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(lines[1].trim(), DATE_TIME_FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(lines[2].trim().toUpperCase());

            if (start.isAfter(end)) {
                end = end.plusDays(1);
            }
            return Optional.of(new SleepingSession(start, end, quality));

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}