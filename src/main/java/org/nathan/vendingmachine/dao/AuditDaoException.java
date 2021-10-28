package org.nathan.vendingmachine.dao;

public class AuditDaoException extends Exception{
    public AuditDaoException(String message) {
        super(message);
    }

    public AuditDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
