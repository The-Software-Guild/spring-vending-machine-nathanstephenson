package org.nathan.vendingmachine.service;

import java.math.BigDecimal;

public enum Currency {
    PENNY(new BigDecimal("0.01")),
    TWOPENCE(new BigDecimal("0.02")),
    FIVEPENCE(new BigDecimal("0.05")),
    TENPENCE(new BigDecimal("0.10")),
    TWENTYPENCE(new BigDecimal("0.20")),
    FIFTYPENCE(new BigDecimal("0.50")),
    POUND(new BigDecimal("1.00")),
    TWOPOUND(new BigDecimal("2.00"));

    private BigDecimal value;//in pounds

    Currency(BigDecimal value){
        this.value = value;
    }

    public BigDecimal getValue(){
        return value;
    }

}
