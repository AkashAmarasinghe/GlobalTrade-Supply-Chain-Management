package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="vendor_performance")
public class VendorPerformance implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="vendor_id", nullable=false) private Vendor vendor;
    @Column(name="evaluation_date", nullable=false) private LocalDate evaluationDate;
    @Column(name="delivery_score", precision=5, scale=2) private BigDecimal deliveryScore=BigDecimal.ZERO;
    @Column(name="quality_score", precision=5, scale=2) private BigDecimal qualityScore=BigDecimal.ZERO;
    @Column(name="compliance_score", precision=5, scale=2) private BigDecimal complianceScore=BigDecimal.ZERO;
    @Column(name="overall_score", precision=5, scale=2) private BigDecimal overallScore=BigDecimal.ZERO;
    @Column(length=500) private String remarks;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ if(evaluationDate==null)evaluationDate=LocalDate.now(); if(createdAt==null)createdAt=LocalDateTime.now(); }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Vendor getVendor(){return vendor;} public void setVendor(Vendor v){vendor=v;}
    public LocalDate getEvaluationDate(){return evaluationDate;} public void setEvaluationDate(LocalDate v){evaluationDate=v;}
    public BigDecimal getDeliveryScore(){return deliveryScore;} public void setDeliveryScore(BigDecimal v){deliveryScore=v;}
    public BigDecimal getQualityScore(){return qualityScore;} public void setQualityScore(BigDecimal v){qualityScore=v;}
    public BigDecimal getComplianceScore(){return complianceScore;} public void setComplianceScore(BigDecimal v){complianceScore=v;}
    public BigDecimal getOverallScore(){return overallScore;} public void setOverallScore(BigDecimal v){overallScore=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
