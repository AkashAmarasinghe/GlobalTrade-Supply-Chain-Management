package com.globaltrade;

import com.globaltrade.bean.CustomsBean;
import com.globaltrade.bean.InventoryReconciliationBean;
import com.globaltrade.bean.ShipmentBean;
import com.globaltrade.bean.VendorBean;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import com.globaltrade.interceptor.TradeComplianceInterceptor;
import com.globaltrade.security.AuthorizationBean;
import com.globaltrade.timer.CustomsDeadlineTimer;
import com.globaltrade.timer.InventoryMonitoringTimer;
import com.globaltrade.timer.RouteOptimizationTimer;
import com.globaltrade.timer.ShipmentTrackingTimer;
import com.globaltrade.timer.VendorPerformanceTimer;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.Schedule;
import jakarta.ejb.Timeout;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.interceptor.Interceptors;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedEjbConfigurationTest {

    @Test
    void persistentDeclarativeTimersAreConfigured() throws Exception {
        assertPersistentSchedule(ShipmentTrackingTimer.class, "pollShipmentStatus");
        assertPersistentSchedule(InventoryMonitoringTimer.class, "checkLowStock");
        assertPersistentSchedule(VendorPerformanceTimer.class, "evaluateThresholds");
        assertPersistentSchedule(RouteOptimizationTimer.class, "calculateRouteWindow");

        Method timeout = Arrays.stream(CustomsDeadlineTimer.class.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Timeout.class))
                .findFirst()
                .orElse(null);
        assertNotNull(timeout, "Programmatic customs timer requires an @Timeout callback");
    }

    @Test
    void containerAndBeanManagedTransactionStrategiesArePresent() throws Exception {
        TransactionManagement shipmentTm = ShipmentBean.class.getAnnotation(TransactionManagement.class);
        assertNotNull(shipmentTm);
        assertEquals(TransactionManagementType.CONTAINER, shipmentTm.value());

        TransactionAttribute create = ShipmentBean.class
                .getMethod("create", com.globaltrade.entity.Shipment.class, Long.class, Long.class, java.util.List.class)
                .getAnnotation(TransactionAttribute.class);
        assertNotNull(create);
        assertEquals(TransactionAttributeType.REQUIRED, create.value());

        TransactionManagement reconcileTm = InventoryReconciliationBean.class.getAnnotation(TransactionManagement.class);
        assertNotNull(reconcileTm);
        assertEquals(TransactionManagementType.BEAN, reconcileTm.value());
    }

    @Test
    void interceptorChainContainsAuditAndPerformanceConcerns() {
        Interceptors interceptors = VendorBean.class.getAnnotation(Interceptors.class);
        assertNotNull(interceptors);
        assertTrue(Arrays.asList(interceptors.value()).contains(PerformanceInterceptor.class));
        assertTrue(Arrays.asList(interceptors.value()).contains(AuditInterceptor.class));

        Interceptors customsInterceptors = CustomsBean.class.getAnnotation(Interceptors.class);
        assertNotNull(customsInterceptors);
        assertTrue(Arrays.asList(customsInterceptors.value()).contains(TradeComplianceInterceptor.class));
    }

    @Test
    void requiredSecurityRolesAreDeclared() {
        DeclareRoles roles = AuthorizationBean.class.getAnnotation(DeclareRoles.class);
        assertNotNull(roles);
        assertTrue(Arrays.asList(roles.value()).containsAll(Arrays.asList(
                "ADMIN", "LOGISTICS_COORDINATOR", "WAREHOUSE_MANAGER",
                "CUSTOMS_AGENT", "VENDOR", "CUSTOMER")));
    }

    private void assertPersistentSchedule(Class<?> type, String methodName) throws Exception {
        Schedule schedule = type.getMethod(methodName).getAnnotation(Schedule.class);
        assertNotNull(schedule, methodName + " must be a declarative EJB timer");
        assertTrue(schedule.persistent(), methodName + " must use a persistent timer");
    }
}
