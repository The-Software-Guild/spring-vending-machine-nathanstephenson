package org.nathan.vendingmachine.controller;

import org.nathan.vendingmachine.dao.AuditDaoException;
import org.nathan.vendingmachine.dao.VendingMachineDaoException;
import org.nathan.vendingmachine.dto.Snack;
import org.nathan.vendingmachine.service.Currency;
import org.nathan.vendingmachine.service.InsufficientFundsException;
import org.nathan.vendingmachine.service.VendingMachineServiceLayer;
import org.nathan.vendingmachine.ui.VendingMachineView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Component
public class VendingMachineController {
    private final String FILENAME = "vendingmachine";
    private final VendingMachineServiceLayer sl;
    private final VendingMachineView view;
    private BigDecimal funds;

    @Autowired
    public VendingMachineController(VendingMachineServiceLayer service, VendingMachineView view) {
        this.sl = service;
        this.view = view;
        this.funds = new BigDecimal("0");
    }

    public void run() throws VendingMachineDaoException, AuditDaoException, InsufficientFundsException {
        boolean cont = true;
        File f = new File(FILENAME + ".txt");
        if (f.exists()) {
            sl.loadStock(FILENAME);
        }
        while (cont) {
            switch (view.menu(funds)) {
                case 0:
                    cont = false;
                    sl.saveStock(FILENAME);
                    break;
                case 1:
                    List <Currency> coins = view.insertCoins();
                    funds = funds.add(sl.fundsValue(coins));
                    break;
                case 2:
                    if(sl.getSnacks().size() > 0){
                        int purchaseIndex = view.purchaseSnack(sl.getSnacks());
                        if (funds.compareTo(sl.getSnack(purchaseIndex).getPrice()) > 0) {
                            funds = funds.subtract(sl.getSnack(purchaseIndex).getPrice());
                            sl.purchaseSnack(purchaseIndex);
                        } else {
                            throw new InsufficientFundsException("Insufficient funds");
                        }
                    } else {
                        System.out.println("There are no snacks currently in stock.");
                    }
                    break;
                case 3:
                    switch (view.editStock()) {
                        case 1:
                            boolean existsToAdd;
                            do {
                                Snack newSnack = view.addSnack();
                                existsToAdd = sl.validateSnackExists(newSnack.getName());
                                if (!existsToAdd) {
                                    sl.addSnack(newSnack);
                                }
                            } while (existsToAdd);
                            break;
                        case 2:
                            boolean existsToRemove;
                            do {
                                int snackIndex = view.removeSnack(sl.getSnacks());
                                existsToRemove = sl.validateSnackExists(snackIndex);
                                if (existsToRemove) {
                                    sl.removeSnack(snackIndex);
                                }
                            } while (!existsToRemove);
                            break;
                        case 3:
                            int editKey;
                            do {
                                editKey = view.editSnackSelection(sl.getSnacks());
                                if (!sl.validateSnackExists(editKey)) {
                                    System.err.println("Invalid selection.");
                                }
                            } while (!sl.validateSnackExists(editKey));
                            Snack editSnack = view.editSnack(sl.getSnack(editKey));
                            sl.editSnack(editKey, editSnack);
                            break;
                        case 4:
                            sl.saveStock(view.saveStock());
                            break;
                        case 5:
                            sl.loadStock(view.loadStock());
                            break;
                        case 0:
                            break;
                    }
                    break;
            }
        }
    }
}
