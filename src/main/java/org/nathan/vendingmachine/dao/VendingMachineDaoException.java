package org.nathan.vendingmachine.dao;

public class VendingMachineDaoException extends Exception {
    public VendingMachineDaoException(String message) {
        super(message);
    }

    public VendingMachineDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
