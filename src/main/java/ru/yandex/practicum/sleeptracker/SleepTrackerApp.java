package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analytics.*;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyticFunctions =
            List.of(new SessionCounter(),
                    new MinDurationSession(),
                    new MaxDurationSession(),
                    new AverageDurationSession(),
                    new BadSleepCounter(),
                    new SleeplessNight(),
                    new ChronotypeAnalyzer());

    public static void main(String[] args) {
        SleepTrackerApp tracker = new SleepTrackerApp();
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу с логом сна в аргументах запуска.");
            return;
        }


        try {
            String filePath = args[0];
            System.out.println("Файл " + filePath + " получен");

            List<SleepingSession> sessions = tracker.readFile(getFile(filePath));
            List<SleepAnalysisResult> result = tracker.analyzeSessions(sessions);

            result.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла " + e.getMessage());
        }


    }

    List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return analyticFunctions.stream()
                .map(function -> function.apply(sessions))
                .collect(Collectors.toList());
    }

    private static File getFile(String filePath) throws FileNotFoundException {


        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Не существует файл " + file);
        }
        return file;
    }

    private List<SleepingSession> readFile(File file) {

        List<SleepingSession> sessions = new ArrayList<>();

        try (Stream<String> logLines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            sessions = logLines.map(this::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

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