package com.globaltrade.bean;

import com.globaltrade.entity.Vendor;
import com.globaltrade.entity.VendorPerformance;
import com.globaltrade.exception.VendorNotFoundException;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import com.globaltrade.util.ScoreUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

@Stateless
@Interceptors({
        PerformanceInterceptor.class,
        AuditInterceptor.class
})
public class VendorPerformanceBean {

    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @EJB
    private AlertBean alertBean;

    public VendorPerformance evaluate(Long vendorId, BigDecimal delivery, BigDecimal quality,
                                      BigDecimal compliance, String remarks) {
        Vendor vendor = em.find(Vendor.class, vendorId);
        if (vendor == null) {
            throw new VendorNotFoundException("Vendor not found: " + vendorId);
        }

        validateScore("Delivery", delivery);
        validateScore("Quality", quality);
        validateScore("Compliance", compliance);

        VendorPerformance performance = new VendorPerformance();
        performance.setVendor(vendor);
        performance.setDeliveryScore(delivery);
        performance.setQualityScore(quality);
        performance.setComplianceScore(compliance);

        BigDecimal overall = ScoreUtil.average(delivery, quality, compliance);
        performance.setOverallScore(overall);
        performance.setRemarks(remarks == null || remarks.isBlank() ? null : remarks.trim());
        em.persist(performance);
        vendor.setPerformanceScore(overall);

        if (overall.compareTo(BigDecimal.valueOf(70)) < 0) {
            alertBean.createIfAbsent(
                    "MEDIUM",
                    "VENDOR",
                    "Vendor performance review required",
                    vendor.getVendorName() + " score is " + overall,
                    "Vendor",
                    vendor.getId()
            );
        } else {
            alertBean.resolveForReference("VENDOR", "Vendor", vendor.getId());
        }

        return performance;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<VendorPerformance> recent() {
        return em.createQuery(
                "select p from VendorPerformance p join fetch p.vendor order by p.evaluationDate desc, p.id desc",
                VendorPerformance.class
        ).setMaxResults(50).getResultList();
    }

    private void validateScore(String name, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException(name + " score must be between 0 and 100");
        }
    }
}
