package com.example.bankcards.exception.customExceptions.transferExceptions;

public class NotEnoughFundsException extends Exception {
    public NotEnoughFundsException(Long cardId, Double balance) {
        super("Not enough funds on card " + cardId + ". Current balance: " + balance);
    }
}
