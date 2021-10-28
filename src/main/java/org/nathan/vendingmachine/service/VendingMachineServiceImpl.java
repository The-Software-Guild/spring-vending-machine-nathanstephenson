package org.nathan.vendingmachine.service;

import org.nathan.vendingmachine.dao.AuditDao;
import org.nathan.vendingmachine.dao.AuditDaoException;
import org.nathan.vendingmachine.dao.VendingMachineDao;
import org.nathan.vendingmachine.dao.VendingMachineDaoException;
import org.nathan.vendingmachine.dto.Snack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class VendingMachineServiceImpl implements VendingMachineServiceLayer {
    private VendingMachineDao vendingDao;
    private AuditDao auditDao;

    @Autowired
    public VendingMachineServiceImpl(VendingMachineDao vendingDao, AuditDao auditDao) {
        this.vendingDao = vendingDao;
        this.auditDao = auditDao;
    }

    //CRUD operations
    @Override
    public Snack getSnack(int i) throws VendingMachineDaoException {
        return vendingDao.getSnack(i);
    }

    @Override
    public Map<Integer, Snack> getSnacks() throws VendingMachineDaoException {
        return vendingDao.getMachineStock();
    }

    @Override
    public void addSnack(Snack snack) throws VendingMachineDaoException, AuditDaoException {
        vendingDao.addSnack(snack.getName(), snack.getCount(), snack.getPrice());
        logAudit("Added snack '" + snack.getName() + "'");
    }

    @Override
    public void removeSnack(int i) throws VendingMachineDaoException, AuditDaoException {
        if (validateSnackExists(i)) {
            String name = vendingDao.getSnack(i).getName();
            vendingDao.removeSnack(i);
            logAudit("Removed snack '" + name + "'");
        } else {
            System.err.println("Snack does not exist.");
        }
    }

    @Override
    public void editSnack(int i, Snack snack) throws VendingMachineDaoException, AuditDaoException {
        if (validateSnackExists(i)) {
            vendingDao.editSnack(i, snack.getName(), snack.getCount(), snack.getPrice());
            logAudit("Edited snack '" + snack.getName() + "'");
        } else {
            System.err.println("Snack does not exist.");
        }
    }

    //service-specific functions
    @Override
    public void purchaseSnack(int i) throws VendingMachineDaoException, AuditDaoException {
        Snack s = vendingDao.getSnack(i);
        if (s.getCount() > 1) {
            vendingDao.editSnack(i, s.getName(), s.getCount() - 1, s.getPrice());
            logAudit(s.getName() + " purchased.");
        } else {
            vendingDao.removeSnack(i);
            logAudit(s.getName() + " purchased. Now out of stock");
        }
    }

    @Override
    public void loadStock(String filename) throws VendingMachineDaoException {
        try {
            vendingDao.loadMachineStock(filename);
        } catch (Exception e) {
            throw new VendingMachineDaoException("Error loading stock");
        }
    }

    @Override
    public void saveStock(String filename) throws VendingMachineDaoException {
        try {
            vendingDao.saveMachineStock(filename);
        } catch (Exception e) {
            throw new VendingMachineDaoException("Error saving stock");
        }
    }

    @Override
    public void logAudit(String operation) throws AuditDaoException {
        auditDao.logAudit(operation);
    }

    @Override
    public boolean validateSnackExists(int i) throws VendingMachineDaoException {
        return vendingDao.getMachineStock().containsKey(i);
    }

    @Override
    public boolean validateSnackExists(String name) throws VendingMachineDaoException {
        boolean exists = false;
        for (Snack s : vendingDao.getMachineStock().values()) {
            exists = s.getName().equals(name) || exists;
        }
        return exists;
    }

    @Override
    public boolean validateSnackSelection(int choice) throws VendingMachineDaoException {
        return vendingDao.getMachineStock().size() >= choice;
    }

    @Override
    public boolean validateFilename(String filename) {
        return !filename.contains(".");
    }

    @Override
    public BigDecimal fundsValue(List<Currency> coins) {
        BigDecimal total = new BigDecimal("0.00");
        for (Currency c : coins) {
            total = total.add(c.getValue());
        }
        return total;
    }

    @Override
    public List<Currency> getChange(BigDecimal remainingFunds) {
        List<Currency> change = new ArrayList<>();
        BigDecimal remainder = remainingFunds;
        if (remainingFunds.compareTo(Currency.TWOPOUND.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.TWOPOUND);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.TWOPOUND.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.POUND.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.POUND);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.POUND.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.FIFTYPENCE.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.FIFTYPENCE);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.FIFTYPENCE.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.TWENTYPENCE.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.TWENTYPENCE);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.TWENTYPENCE.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.TENPENCE.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.TENPENCE);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.TENPENCE.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.FIVEPENCE.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.FIVEPENCE);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.FIVEPENCE.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.TWOPENCE.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.TWOPENCE);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.TWOPENCE.getValue().multiply(new BigDecimal(returned.size())));
        }
        if (remainder.compareTo(Currency.PENNY.getValue()) >= 0) {
            List<Currency> returned = getChangeCoins(remainder, Currency.PENNY);
            change.addAll(returned);
            remainder = remainder.subtract(Currency.PENNY.getValue().multiply(new BigDecimal(returned.size())));
        }
        return change;
    }

    private List<Currency> getChangeCoins(BigDecimal funds, Currency coin){
        String num = String.valueOf(Math.floor(funds.divide(coin.getValue()).doubleValue()));
        int count = Integer.parseInt(num.substring(0, num.indexOf('.')));
        List<Currency> change = new ArrayList<>();
        for(int i = 0; i < count; i++){
            change.add(coin);
        }
        return change;
    }

    @Override
    public boolean sufficientFunds(List<Currency> coins, BigDecimal price) {
        return fundsValue(coins).compareTo(price) >= 0;
    }


}
