package org.nathan.vendingmachine.service;

import org.nathan.vendingmachine.dao.AuditDaoException;
import org.nathan.vendingmachine.dao.VendingMachineDaoException;
import org.nathan.vendingmachine.dto.Snack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VendingMachineServiceLayer {
    Snack getSnack(int i) throws VendingMachineDaoException;

    Map<Integer, Snack> getSnacks() throws VendingMachineDaoException;

    void addSnack(Snack snack) throws VendingMachineDaoException, AuditDaoException;

    void removeSnack(int i) throws VendingMachineDaoException, AuditDaoException;

    void editSnack(int i, Snack snack) throws VendingMachineDaoException, AuditDaoException;

    void purchaseSnack(int i) throws VendingMachineDaoException, AuditDaoException;

    void loadStock(String filename) throws VendingMachineDaoException;

    void saveStock(String filename) throws VendingMachineDaoException;

    void logAudit(String operation) throws AuditDaoException;

    boolean validateSnackExists(int i) throws VendingMachineDaoException;

    boolean validateSnackExists(String name) throws VendingMachineDaoException;

    boolean validateSnackSelection(int choice) throws VendingMachineDaoException;

    boolean validateFilename(String filename);

    BigDecimal fundsValue(List<Currency> coins);

    List<Currency> getChange(BigDecimal remainingFunds);

    boolean sufficientFunds(List<Currency> coins, BigDecimal price);
}
