package com.example.bankcards.exception.customExceptions.cardExceptions;

public class CardAlreadyExistsException extends Exception {
    public CardAlreadyExistsException(String cardNumber) {
        super("Card already exists with number: " + cardNumber);
    }
}
