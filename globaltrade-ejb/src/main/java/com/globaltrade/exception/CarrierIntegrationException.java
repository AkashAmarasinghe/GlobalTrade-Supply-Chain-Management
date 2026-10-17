package com.globaltrade.exception;
import jakarta.ejb.ApplicationException;
@ApplicationException(rollback=false) public class CarrierIntegrationException extends RuntimeException {
    public CarrierIntegrationException(String m) {
        super(m);
    }
    public CarrierIntegrationException(String m,Throwable t) {
        super(m,t);
    }
}
