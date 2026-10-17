package com.globaltrade.entity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="vendors") public class Vendor implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="vendor_code", nullable=false, unique=true, length=30) private String vendorCode;
    @Column(name="vendor_name", nullable=false, length=120) private String vendorName;
    @Column(name="contact_person", length=100) private String contactPerson;
    @Column(length=120) private String email;
    @Column(length=30) private String phone;
    @Column(length=80) private String country;
    @Column(nullable=false, length=30) private String status="ACTIVE";
    @Column(name="performance_score", precision=5, scale=2) private BigDecimal performanceScore=BigDecimal.ZERO;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @Column(name="updated_at", nullable=false) private LocalDateTime updatedAt;
    @PrePersist public void prePersist() {
        LocalDateTime n=LocalDateTime.now();
        if(createdAt==null) createdAt=n;
        updatedAt=n;
        if(performanceScore==null) performanceScore=BigDecimal.ZERO;
    }
    @PreUpdate public void preUpdate() {
        updatedAt=LocalDateTime.now();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public String getVendorCode() {
        return vendorCode;
    }
    public void setVendorCode(String v) {
        this.vendorCode=v;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String v) {
        this.vendorName=v;
    }
    public String getContactPerson() {
        return contactPerson;
    }
    public void setContactPerson(String v) {
        this.contactPerson=v;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String v) {
        this.email=v;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String v) {
        this.phone=v;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String v) {
        this.country=v;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String v) {
        this.status=v;
    }
    public BigDecimal getPerformanceScore() {
        return performanceScore;
    }
    public void setPerformanceScore(BigDecimal v) {
        this.performanceScore=v;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
