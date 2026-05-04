package ru.tbank.practicum.exception;

public class DeviceNotFoundException extends EntityNotFoundException {
    public DeviceNotFoundException(Long id) {
        super("Устройство с id " + id + " не найдено");
    }
}
