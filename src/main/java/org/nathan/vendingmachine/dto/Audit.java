package org.nathan.vendingmachine.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Audit {
    private LocalDateTime date;
    private String operation;

    public Audit(LocalDateTime date, String operation){
        this.date = date;
        this.operation = operation;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String toString(){
        return date.format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")) + ": " + operation;
    }
}
