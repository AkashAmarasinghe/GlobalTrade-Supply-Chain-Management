package com.globaltrade.interceptor;

import com.globaltrade.entity.CustomsDocument;
import com.globaltrade.exception.CustomsComplianceException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.time.LocalDateTime;

@Interceptor
public class TradeComplianceInterceptor {

    @AroundInvoke
    public Object validate(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();
        if (parameters != null) {
            for (Object parameter : parameters) {
                if (parameter instanceof CustomsDocument document) {
                    validateDocument(document);
                }
            }
        }
        return context.proceed();
    }

    private void validateDocument(CustomsDocument document) {
        if (document.getDocumentType() == null || document.getDocumentType().isBlank()) {
            throw new CustomsComplianceException("Customs document type is required");
        }
        if (document.getDueDate() != null && document.getDueDate().isBefore(LocalDateTime.now())) {
            throw new CustomsComplianceException("Customs deadline cannot be in the past");
        }
    }
}
