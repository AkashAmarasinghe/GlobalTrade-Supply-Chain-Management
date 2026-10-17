package com.globaltrade;

import com.globaltrade.security.AuthorizationBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationBeanTest {

    private final AuthorizationBean authorization = new AuthorizationBean();

    @Test
    void adminCanAccessAllApplicationAreas() {
        assertTrue(authorization.isAuthorized("ADMIN", "/vendors", "POST"));
        assertTrue(authorization.isAuthorized("ADMIN", "/inventory", "POST"));
        assertTrue(authorization.isAuthorized("ADMIN", "/customs", "POST"));
        assertTrue(authorization.isAuthorized("ADMIN", "/access-control", "GET"));
    }

    @Test
    void warehouseManagerCanManageInventoryButNotVendorAdministration() {
        assertTrue(authorization.isAuthorized("WAREHOUSE_MANAGER", "/inventory", "POST"));
        assertTrue(authorization.isAuthorized("WAREHOUSE_MANAGER", "/shipments", "GET"));
        assertFalse(authorization.isAuthorized("WAREHOUSE_MANAGER", "/vendors", "POST"));
        assertFalse(authorization.isAuthorized("WAREHOUSE_MANAGER", "/access-control", "GET"));
    }

    @Test
    void customsAgentHasCustomsAndReadOnlyShipmentAccess() {
        assertTrue(authorization.isAuthorized("CUSTOMS_AGENT", "/customs", "POST"));
        assertTrue(authorization.isAuthorized("CUSTOMS_AGENT", "/shipments", "GET"));
        assertFalse(authorization.isAuthorized("CUSTOMS_AGENT", "/shipments", "POST"));
    }

    @Test
    void logisticsCoordinatorCanRecordVendorPerformance() {
        assertTrue(authorization.isAuthorized("LOGISTICS_COORDINATOR", "/vendor-performance", "GET"));
        assertTrue(authorization.isAuthorized("LOGISTICS_COORDINATOR", "/vendor-performance", "POST"));
    }
}
