package com.globaltrade.exception;
import jakarta.ejb.ApplicationException;
@ApplicationException(rollback=true) public class CustomsComplianceException extends RuntimeException {
    public CustomsComplianceException(String m) {
        super(m);
    }
}
