package org.nathan.vendingmachine.service;

import org.nathan.vendingmachine.dao.*;
import org.nathan.vendingmachine.dto.Audit;
import org.nathan.vendingmachine.dto.Snack;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineServiceLayerTest {
    private VendingMachineDao vendingDao;
    private AuditDao auditDao;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws VendingMachineDaoException {
        this.vendingDao = new VendingMachineDao(){
            private static final String DELIMITER = "::";
            private Map<Integer, Snack> snacks = new HashMap<>();
            private int index;

            @Override
            public void loadMachineStock(String filename) throws VendingMachineDaoException {
                Map<Integer, Snack> loadedSnacks = new HashMap<>();
                try {
                    Scanner in = new Scanner(new BufferedReader(new FileReader(filename + ".txt")));
                    int i = 1;
                    while (in.hasNextLine()) {
                        Snack s = unmarshallSnack(in.nextLine());
                        if(s.getCount() > 0){
                            loadedSnacks.put(i, s);
                        }
                        i++;
                    }
                    index = i;
                } catch (Exception e) {
                    throw new VendingMachineDaoException("Error loading from persistent storage.");
                }
                snacks = loadedSnacks;
            }

            @Override
            public void saveMachineStock(String filename) throws VendingMachineDaoException {
                try {
                    PrintWriter out = new PrintWriter(new FileWriter(filename + ".txt"));
                    for (Snack s : snacks.values()) {
                        if(s.getCount() > 0){
                            out.println(marshallSnack(s));
                        }
                        out.flush();
                    }
                    out.close();
                } catch (Exception e) {
                    throw new VendingMachineDaoException("Error saving data.");
                }
            }

            @Override
            public Map<Integer, Snack> getMachineStock() throws VendingMachineDaoException {
                try {
                    return snacks;
                } catch (Exception e) {
                    throw new VendingMachineDaoException("Error accessing data.");
                }
            }

            @Override
            public Snack getSnack(int i) throws VendingMachineDaoException {
                try {
                    return snacks.get(i);
                } catch (Exception e) {
                    throw new VendingMachineDaoException("Error retrieving data.");
                }
            }

            @Override
            public void addSnack(String name, int count, BigDecimal price) throws VendingMachineDaoException {
                try {
                    snacks.put(++index, new Snack(name, count, price));
                } catch (Exception e) {
                    throw new VendingMachineDaoException("Error adding snack to collection.");
                }
            }

            @Override
            public boolean removeSnack(int i) {
                if (snacks.containsKey(i)) {
                    snacks.remove(i);
                    return true;
                }
                System.err.println("Snack does not exist to be removed.");
                return false;
            }

            @Override
            public boolean editSnack(int i, String newName, int newCount, BigDecimal newPrice) throws VendingMachineDaoException {
                if (snacks.containsKey(i)) {
                    try {
                        snacks.replace(i, new Snack(newName, newCount, newPrice));
                    } catch (Exception e) {
                        throw new VendingMachineDaoException("Error updating snack.");
                    }
                    return true;
                }
                System.err.println("Snack does not exist to be edited.");
                return false;
            }

            private String marshallSnack(Snack snack) {
                return snack.getName() + DELIMITER + snack.getCount() + DELIMITER + snack.getPrice();
            }

            private Snack unmarshallSnack(String str) {
                String[] data = str.split(DELIMITER);
                return new Snack(data[0], Integer.parseInt(data[1]), new BigDecimal(data[2]));
            }
        };
        this.auditDao = new AuditDao(){
            @Override
            public void setFilename(String filename) {}

            @Override
            public void logAudit(String operation) throws AuditDaoException {
                try {
                    PrintWriter out = new PrintWriter(new FileWriter("filename" + ".txt", true));
                    out.println(new Audit(LocalDateTime.now(), operation));
                    out.flush();
                    out.close();
                }catch (Exception e){
                    throw new AuditDaoException("Failed to write to audit log.");
                }
            }
        };

        vendingDao.addSnack("Mars", 5, new BigDecimal("0.70"));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        vendingDao.removeSnack(2);
    }

    @org.junit.jupiter.api.Test
    void getSnack() throws VendingMachineDaoException {
        Snack s = vendingDao.getSnack(1);
        assertNotNull(s);
    }

    @org.junit.jupiter.api.Test
    void getSnacks() throws VendingMachineDaoException {
        assertTrue(vendingDao.getMachineStock().size() > 0);
    }

    @org.junit.jupiter.api.Test
    void addSnack() throws VendingMachineDaoException {
        vendingDao.addSnack("KitKat", 2, new BigDecimal("1.20"));
        System.out.println(vendingDao.getMachineStock().size());
        assertNotNull(vendingDao.getSnack(2));
    }

    @org.junit.jupiter.api.Test
    void removeSnack() {
    }

    @org.junit.jupiter.api.Test
    void editSnack() {
    }

    @org.junit.jupiter.api.Test
    void loadStock() {
    }

    @org.junit.jupiter.api.Test
    void saveStock() {
    }

    @org.junit.jupiter.api.Test
    void validateSnackExists() {
    }

    @org.junit.jupiter.api.Test
    void testValidateSnackExists() {
    }

    @org.junit.jupiter.api.Test
    void validateSnackSelection() {
    }

    @org.junit.jupiter.api.Test
    void validateFilename() {
    }

    @org.junit.jupiter.api.Test
    void fundsValue() {
    }

    @org.junit.jupiter.api.Test
    void sufficientFunds() {
    }
}