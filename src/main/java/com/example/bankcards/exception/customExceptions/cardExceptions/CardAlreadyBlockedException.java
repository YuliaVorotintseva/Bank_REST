package com.example.bankcards.exception.customExceptions.cardExceptions;

public class CardAlreadyBlockedException extends Exception {
    public CardAlreadyBlockedException(Long cardId) {
        super("Card already blocked with id: " + cardId);
    }
}
