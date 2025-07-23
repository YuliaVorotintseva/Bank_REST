package com.example.bankcards.exception.customExceptions.transferExceptions;

public class SameCardTransferException extends Exception {
    public SameCardTransferException() {
        super("Cannot transfer to the same card");
    }
}
