package org.nathan.vendingmachine.ui;

import org.nathan.vendingmachine.dto.Snack;
import org.nathan.vendingmachine.service.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class VendingMachineView {
    private UserIO io;

    @Autowired
    public VendingMachineView(UserIO userIO) {
        this.io = userIO;
    }

    public int menu(BigDecimal amountInserted) {
        io.print("---Vending Machine (£" + amountInserted + " inserted)---");
        io.print("1 - Insert coins");
        io.print("2 - Purchase Snack");
        io.print("3 - Edit Stock");
        io.print("0 - Exit");
        return io.readInt("Please choose the operation to perform:", 0, 3);
    }

    public List<Currency> insertCoins() {
        List<Currency> coins = new ArrayList<>();
        boolean cont;
        do {
            coins.add(io.readCurrency("Please enter the name of the coin you would like to insert:"));
            cont = io.readBool("Add another coin?");
        } while (cont);
        return coins;
    }

    public int purchaseSnack(Map<Integer, Snack> snacks) {
        printSnacks(snacks);
        int choice;
        boolean validChoice = false;
        do {
            choice = io.readInt("Choice: ");
            for (int key : snacks.keySet()) {
                validChoice = choice == key || validChoice;
            }
            System.out.println(validChoice ? "" : "Please select a valid snack.");
        } while (!validChoice);
        return choice;
    }

    public int editStock(){
        io.print("--Edit Stock--");
        io.print("1 - Add Snack");
        io.print("2 - Remove Snack");
        io.print("3 - Edit Snack");
        io.print("4 - Save Stock");
        io.print("5 - Load Stock");
        io.print("0 - Back");
        return io.readInt("Please choose the operation to perform:", 0, 5);
    }

    public Snack addSnack(){
        return new Snack(io.readString("What is the name of the snack?"), io.readInt("How many of this snack is available?"), io.readBigDecimal("What is the price of the snack?"));
    }

    public int removeSnack(Map<Integer, Snack> snacks){
        printSnacks(snacks);
        return io.readInt("Please enter the index of the snack you would like to remove:");
    }

    public Snack editSnack(Snack snack){
        String name = snack.getName();
        int count = snack.getCount();
        BigDecimal price = snack.getPrice();
        boolean cont = true;
        while(cont){
            io.print("--Editing " + snack.getName() + ":");
            io.print("1 - Edit name (" + snack.getName() + " -> " + name + ")");
            io.print("2 - Edit count (" + snack.getCount() + " -> " + count + ")");
            io.print("3 - Edit price (£" + snack.getPrice() + " -> " + " £" + price + ")");
            io.print("0 - Submit");
            switch (io.readInt("Choose which property to edit:", 0, 3)){
                case 1:
                    name = io.readString("What is the new name of " + snack.getName() + "?");
                    break;
                case 2:
                    count = io.readInt("What is the new count of " + snack.getName() + "?");
                    break;
                case 3:
                    price = io.readBigDecimal("What is the new price for " + snack.getName() + "?");
                    break;
                case 0:
                    cont = false;
                    break;
            }
        }
        return new Snack(name, count, price);
    }

    public int editSnackSelection(Map<Integer, Snack> snacks){
        printSnacks(snacks);
        return io.readInt("Please enter the index of the snack you would like to edit:");
    }

    public String loadStock() {
        return io.readString("What is the name of the file to load from?");
    }

    public String saveStock() {
        return io.readString("What is the name of the file to save to?");
    }

    private void printSnacks(Map<Integer, Snack> snacks){
        for (Map.Entry<Integer, Snack> s : snacks.entrySet()) {
            io.print(s.getKey() + " - " + s.getValue().getName() + " (" + s.getValue().getCount() + " remaining) - " + s.getValue().getPrice().doubleValue());
        }
    }
}
