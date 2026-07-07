package com.sisencodigital.dashboard.exceptions;

import com.sisencodigital.dashboard.exceptions.custom.InvalidUserRoleException;
import com.sisencodigital.dashboard.exceptions.custom.UserAlreadyExistsException;
import com.sisencodigital.dashboard.util.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles @Valid failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.error(
                        "Bad Request",
                        null,
                        errors
                ));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError<Void>> handleUserNotFoundError(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.error(
                        "User Not Found",
                        ex.getMessage(),
                        null
                ));
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError<Void>> handleUserAlreadyExistError(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.error(
                        "User Already Exists",
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ApiError<Void>> handleInvalidUserRoleError(InvalidUserRoleException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.error(
                        "Invalid User Role",
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError<Void>> handleBadCredentialsError(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.error(
                        "Bad Credentials",
                        "Invalid Password please try again",
                        null
                ));
    }

    // Handles IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.error(
                        "Bad Request",
                        ex.getMessage(),
                        null
                ));
    }

    // Handles Authentication exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError<Void>> handleAuthenticationErrors(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.error(
                        "Unauthorized",
                        ex.getMessage(),
                        null
                ));
    }

    // Handles Access Denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError<Void>> handleAccessDeniedErrors(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.error(
                        "Forbidden",
                        ex.getMessage() + ": You do not have permission to access this resource.",
                        null
                ));
    }


    // Handles Business/Runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError<Void>> handleRuntimeErrors(RuntimeException ex) {
        log.error("Unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.error(
                        "Internal Server Error",
                        "An unexpected error occurred. Please try again later.",
                        null

                ));
    }
}
