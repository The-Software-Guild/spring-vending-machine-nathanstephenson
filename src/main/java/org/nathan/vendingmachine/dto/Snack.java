package org.nathan.vendingmachine.dto;

import java.math.BigDecimal;

public class Snack {
    private final String name;
    private int count;
    private BigDecimal price;

    public Snack(String name) {
        this.name = name;
    }

    public Snack(String name, int count, BigDecimal price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
