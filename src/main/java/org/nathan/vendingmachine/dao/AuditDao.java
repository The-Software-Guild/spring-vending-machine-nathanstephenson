package org.nathan.vendingmachine.dao;

import org.nathan.vendingmachine.dto.Audit;

import java.io.IOException;

public interface AuditDao {
    void setFilename(String filename);
    void logAudit(String operation) throws AuditDaoException;
}
