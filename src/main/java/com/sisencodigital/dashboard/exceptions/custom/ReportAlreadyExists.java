package com.sisencodigital.dashboard.exceptions.custom;

public class ReportAlreadyExists extends RuntimeException {
    public ReportAlreadyExists(String message) {
        super(message);
    }
}
