package ru.tbank.practicum.exception;

public class ScheduleNotFoundException extends EntityNotFoundException {
    public ScheduleNotFoundException(Long id) {
        super("Расписание с id " + id + " не найдено");
    }
}
