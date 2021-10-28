package org.nathan.vendingmachine.ui;

import org.nathan.vendingmachine.service.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements UserIO {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        boolean inRange;
        int input;
        do {
            System.out.println(prompt);
            input = Integer.parseInt(scanner.nextLine());
            inRange = input >= min && input <= max;
        } while (!inRange);
        return input;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        System.out.println(prompt);
        String input = Double.parseDouble(scanner.nextLine()) + "";
        return new BigDecimal(input);
    }

    @Override
    public Currency readCurrency(String prompt) {
        boolean validInput = false;
        System.out.println(prompt);
        for (Currency c : Currency.values()) {
            System.out.println(c.name());
        }
        String input;
        do {
            input = scanner.nextLine().toUpperCase(Locale.ROOT);
            for (Currency c : Currency.values()) {
                validInput = c.name().equals(input) || validInput;
            }
            System.out.println(validInput ? "" : "Please choose a valid coin to insert");
        } while (!validInput);
        return Currency.valueOf(input);
    }

    @Override
    public boolean readBool(String prompt) {
        System.out.println(prompt + " (y/n)");
        return scanner.nextLine().charAt(0) == 'y';
    }


}
