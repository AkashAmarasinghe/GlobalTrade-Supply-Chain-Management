package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="system_alerts")
public class SystemAlert implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=20) private String severity="INFO";
    @Column(nullable=false, length=50) private String category;
    @Column(nullable=false, length=150) private String title;
    @Column(nullable=false, length=1000) private String message;
    @Column(name="reference_type", length=80) private String referenceType;
    @Column(name="reference_id") private Long referenceId;
    @Column(nullable=false) private boolean resolved=false;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @Column(name="resolved_at") private LocalDateTime resolvedAt;
    @PrePersist public void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getSeverity(){return severity;} public void setSeverity(String v){severity=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public String getReferenceType(){return referenceType;} public void setReferenceType(String v){referenceType=v;}
    public Long getReferenceId(){return referenceId;} public void setReferenceId(Long v){referenceId=v;}
    public boolean isResolved(){return resolved;} public void setResolved(boolean v){resolved=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getResolvedAt(){return resolvedAt;} public void setResolvedAt(LocalDateTime v){resolvedAt=v;}
}
