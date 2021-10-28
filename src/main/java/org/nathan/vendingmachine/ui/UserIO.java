package org.nathan.vendingmachine.ui;

import org.nathan.vendingmachine.service.Currency;

import java.math.BigDecimal;

public interface UserIO {
    void print(String message);

    String readString(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    BigDecimal readBigDecimal(String prompt);

    Currency readCurrency(String prompt);

    boolean readBool(String prompt);
}
