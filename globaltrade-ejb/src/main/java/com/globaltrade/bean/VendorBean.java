package com.globaltrade.bean;

import com.globaltrade.entity.Vendor;
import com.globaltrade.exception.VendorNotFoundException;
import com.globaltrade.interceptor.AuditInterceptor;
import com.globaltrade.interceptor.PerformanceInterceptor;
import com.globaltrade.interceptor.VendorValidationInterceptor;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
@Interceptors({
        PerformanceInterceptor.class,
        AuditInterceptor.class,
        VendorValidationInterceptor.class
})
public class VendorBean {

    @PersistenceContext(unitName = "GlobalTradePU")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Vendor> findAll() {
        return em.createQuery(
                "select v from Vendor v order by v.vendorName",
                Vendor.class
        ).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Vendor find(Long id) {
        Vendor vendor = em.find(Vendor.class, id);
        if (vendor == null) {
            throw new VendorNotFoundException("Vendor not found: " + id);
        }
        return vendor;
    }

    public Vendor create(Vendor vendor) {
        em.persist(vendor);
        em.flush();
        return vendor;
    }

    public Vendor updateStatus(Long id, String status) {
        Vendor vendor = find(id);
        vendor.setStatus(status);
        return vendor;
    }
}
