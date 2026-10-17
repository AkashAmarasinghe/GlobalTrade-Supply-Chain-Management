package com.globaltrade.exception;
import jakarta.ejb.ApplicationException;
@ApplicationException(rollback=true) public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String m) {
        super(m);
    }
}
