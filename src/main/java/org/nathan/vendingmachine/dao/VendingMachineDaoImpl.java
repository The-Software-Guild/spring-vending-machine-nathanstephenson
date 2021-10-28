package org.nathan.vendingmachine.dao;

import org.nathan.vendingmachine.dto.Snack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class VendingMachineDaoImpl implements VendingMachineDao {
    private static final String DELIMITER = "::";
    private Map<Integer, Snack> snacks = new HashMap<>();
    private int index;

    @Autowired
    public VendingMachineDaoImpl() {

    }

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
}
