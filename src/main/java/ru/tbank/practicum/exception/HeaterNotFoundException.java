package ru.tbank.practicum.exception;

public class HeaterNotFoundException extends EntityNotFoundException {
    public HeaterNotFoundException(Long id) {
        super("Heater with ID" + id + " not found");
    }
}
