package ru.tbank.practicum.exception;

public class EntityNotFoundExcpetion extends RuntimeException {
    public EntityNotFoundExcpetion(String message) {
        super(message);
    }
}
