package com.globaltrade.interceptor;

import com.globaltrade.audit.AuditContext;
import com.globaltrade.bean.AuditBean;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.reflect.Method;
import javax.naming.InitialContext;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
public class AuditInterceptor {

    private static final Logger LOG =
            Logger.getLogger(AuditInterceptor.class.getName());

    @EJB
    private AuditBean auditBean;

    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object audit(InvocationContext ctx) throws Exception {

        if (!shouldAudit(ctx.getMethod())) {
            return ctx.proceed();
        }

        long start = System.nanoTime();
        boolean success = false;
        String failure = null;
        Object result = null;

        try {

            result = ctx.proceed();
            success = true;

            return result;

        } catch (Exception e) {

            failure = e.getClass().getSimpleName()
                    + ": "
                    + safeMessage(e.getMessage());

            throw e;

        } finally {

            long elapsed =
                    (System.nanoTime() - start) / 1_000_000L;

            try {

                String beanName =
                        ctx.getMethod()
                                .getDeclaringClass()
                                .getSimpleName();

                String methodName =
                        ctx.getMethod().getName();

                String entityType =
                        resolveEntityType(beanName);

                Long entityId =
                        resolveEntityId(
                                result,
                                ctx.getParameters()
                        );

                String details = success
                        ? successDetails(
                        methodName,
                        entityType,
                        ctx.getParameters()
                )
                        : "Failed: " + failure;

                resolveAuditBean().log(
                        resolveActor(),
                        beanName + "." + methodName,
                        entityType,
                        entityId,
                        details,
                        elapsed,
                        success
                );

                AuditContext.markRecorded();

                // Payara evidence log for AuditInterceptor execution
                LOG.info(
                        "AUDIT interceptor executed: "
                                + beanName
                                + "."
                                + methodName
                );

            } catch (Exception auditError) {

                LOG.log(
                        Level.WARNING,
                        "Could not save audit log for "
                                + ctx.getMethod().getName(),
                        auditError
                );
            }
        }
    }

    private AuditBean resolveAuditBean() throws Exception {

        if (auditBean != null) {
            return auditBean;
        }

        auditBean =
                InitialContext.doLookup(
                        "java:module/AuditBean"
                );

        return auditBean;
    }

    private boolean shouldAudit(Method method) {

        String name =
                method.getName()
                        .toLowerCase(Locale.ROOT);

        return !(name.startsWith("find")
                || name.startsWith("get")
                || name.startsWith("list")
                || name.startsWith("count")
                || name.equals("recent"));
    }

    private String resolveActor() {

        String actor = AuditContext.getActor();

        if (actor != null && !actor.isBlank()) {
            return actor;
        }

        try {

            if (sessionContext != null
                    && sessionContext.getCallerPrincipal() != null) {

                String principal =
                        sessionContext
                                .getCallerPrincipal()
                                .getName();

                if (principal != null
                        && !principal.isBlank()
                        && !principal.equalsIgnoreCase("anonymous")) {

                    return principal;
                }
            }

        } catch (Exception ignored) {

        }

        return "SYSTEM";
    }

    private String resolveEntityType(String beanName) {

        return switch (beanName) {

            case "CustomsBean" ->
                    "CustomsDocument";

            case "InventoryBean",
                 "InventoryReconciliationBean" ->
                    "InventoryItem";

            case "ShipmentBean" ->
                    "Shipment";

            case "VendorBean" ->
                    "Vendor";

            case "VendorPerformanceBean" ->
                    "VendorPerformance";

            default ->
                    beanName.endsWith("Bean")
                            ? beanName.substring(
                            0,
                            beanName.length() - 4
                    )
                            : beanName;
        };
    }

    private Long resolveEntityId(
            Object result,
            Object[] parameters
    ) {

        Long resultId = readId(result);

        if (resultId != null) {
            return resultId;
        }

        if (parameters != null) {

            for (Object parameter : parameters) {

                if (parameter instanceof Long id) {
                    return id;
                }
            }
        }

        return null;
    }

    private Long readId(Object value) {

        if (value == null) {
            return null;
        }

        try {

            Object id =
                    value.getClass()
                            .getMethod("getId")
                            .invoke(value);

            return id instanceof Long
                    ? (Long) id
                    : null;

        } catch (Exception ignored) {

            return null;
        }
    }

    private String successDetails(
            String methodName,
            String entityType,
            Object[] parameters
    ) {

        return switch (methodName) {

            case "create" ->
                    entityType
                            + " created successfully";

            case "updateStatus" ->
                    "Status updated to "
                            + parameterText(parameters, 1);

            case "reserveStock" ->
                    "Reserved "
                            + parameterText(parameters, 1)
                            + " unit(s) from inventory";

            case "updateQuantity" ->
                    "Inventory quantity updated to "
                            + parameterText(parameters, 1);

            case "replenishStock" ->
                    "Added "
                            + parameterText(parameters, 1)
                            + " unit(s) to inventory";

            case "reconcile" ->
                    "Physical inventory count reconciled to "
                            + parameterText(parameters, 1);

            case "approve" ->
                    "Customs document approved";

            case "evaluate" ->
                    "Vendor performance evaluation saved";

            default ->
                    entityType
                            + " operation completed";
        };
    }

    private String parameterText(
            Object[] parameters,
            int index
    ) {

        if (parameters == null
                || index < 0
                || index >= parameters.length
                || parameters[index] == null) {

            return "N/A";
        }

        return String.valueOf(parameters[index]);
    }

    private String safeMessage(String message) {

        return message == null || message.isBlank()
                ? "No error message"
                : message;
    }
}