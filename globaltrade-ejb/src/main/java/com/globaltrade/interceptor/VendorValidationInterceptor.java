package com.globaltrade.interceptor;

import com.globaltrade.entity.Vendor;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
public class VendorValidationInterceptor {

    @AroundInvoke
    public Object validate(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();

        if (parameters != null) {
            for (Object parameter : parameters) {
                if (parameter instanceof Vendor vendor) {
                    validateVendor(vendor);
                }
            }
        }

        return context.proceed();
    }

    private void validateVendor(Vendor vendor) {
        if (vendor.getVendorCode() == null || vendor.getVendorCode().isBlank()) {
            throw new IllegalArgumentException("Vendor code is required");
        }
        if (vendor.getVendorName() == null || vendor.getVendorName().isBlank()) {
            throw new IllegalArgumentException("Vendor name is required");
        }
        if (vendor.getEmail() != null && !vendor.getEmail().isBlank() && !vendor.getEmail().contains("@")) {
            throw new IllegalArgumentException("Vendor email is invalid");
        }
    }
}
