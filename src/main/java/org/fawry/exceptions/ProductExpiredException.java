package org.fawry.exceptions;

public class ProductExpiredException extends RuntimeException {
    public ProductExpiredException(String message) {
        super(message);
    }
}
