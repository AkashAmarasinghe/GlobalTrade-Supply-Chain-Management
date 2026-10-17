package com.globaltrade;

import com.globaltrade.exception.CarrierIntegrationException;
import com.globaltrade.exception.CustomsComplianceException;
import com.globaltrade.exception.InsufficientInventoryException;
import com.globaltrade.exception.VendorNotFoundException;
import jakarta.ejb.ApplicationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionConfigurationTest {

    @Test
    void rollbackPolicyMatchesBusinessFailureType() {
        assertRollback(CustomsComplianceException.class, true);
        assertRollback(InsufficientInventoryException.class, true);
        assertRollback(CarrierIntegrationException.class, false);
        assertRollback(VendorNotFoundException.class, false);
    }

    private void assertRollback(Class<?> type, boolean expected) {
        ApplicationException annotation = type.getAnnotation(ApplicationException.class);
        assertNotNull(annotation, type.getSimpleName() + " must be an EJB application exception");
        assertEquals(expected, annotation.rollback());
    }
}
