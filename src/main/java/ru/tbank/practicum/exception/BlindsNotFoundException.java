package ru.tbank.practicum.exception;

public class BlindsNotFoundException extends EntityNotFoundExcpetion {
    public BlindsNotFoundException(Long id) {
        super("Blinds with ID" + id + " not found");
    }
}
