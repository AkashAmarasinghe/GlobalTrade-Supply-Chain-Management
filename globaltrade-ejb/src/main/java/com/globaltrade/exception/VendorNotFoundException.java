package com.globaltrade.exception;
import jakarta.ejb.ApplicationException;
@ApplicationException(rollback=false) public class VendorNotFoundException extends RuntimeException {
    public VendorNotFoundException(String m) {
        super(m);
    }
}
