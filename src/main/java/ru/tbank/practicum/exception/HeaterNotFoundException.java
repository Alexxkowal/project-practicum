package ru.tbank.practicum.exception;

public class HeaterNotFoundException extends EntityNotFoundExcpetion {
    public HeaterNotFoundException(Long id) {
        super("Heater with ID" + id + " not found");
    }
}
