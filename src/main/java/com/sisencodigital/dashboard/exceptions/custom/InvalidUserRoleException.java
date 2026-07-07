package com.sisencodigital.dashboard.exceptions.custom;

public class InvalidUserRoleException extends RuntimeException {
    public InvalidUserRoleException(String message) {
        super(message);
    }
}
