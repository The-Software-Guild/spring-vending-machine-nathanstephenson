package org.nathan.vendingmachine.service;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        System.err.println(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
