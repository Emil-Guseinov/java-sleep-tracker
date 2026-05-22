package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;

public record SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {

}
