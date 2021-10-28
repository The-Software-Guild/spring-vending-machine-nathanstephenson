package org.nathan.vendingmachine.dao;

import org.nathan.vendingmachine.dto.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;

@Component
public class AuditDaoImpl implements AuditDao{
    private String filename;

    @Autowired
    public AuditDaoImpl(@Value("AuditLog")String filename){
        this.filename = filename;
    }

    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void logAudit(String operation) throws AuditDaoException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename + ".txt", true));
            out.println(new Audit(LocalDateTime.now(), operation));
            out.flush();
            out.close();
        }catch (Exception e){
            throw new AuditDaoException("Failed to write to audit log.");
        }
    }
}
