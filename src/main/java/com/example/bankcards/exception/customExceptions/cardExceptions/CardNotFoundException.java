package com.example.bankcards.exception.customExceptions.cardExceptions;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(Long cardId) {
        super("Card not found with id: " + cardId);
    }
}
