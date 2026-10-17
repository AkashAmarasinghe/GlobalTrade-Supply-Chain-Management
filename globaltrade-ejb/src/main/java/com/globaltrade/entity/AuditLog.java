package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_logs")
public class AuditLog implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String actor="SYSTEM";
    @Column(nullable=false, length=150) private String action;
    @Column(name="entity_type", length=100) private String entityType;
    @Column(name="entity_id") private Long entityId;
    @Column(length=1000) private String details;
    @Column(name="execution_time_ms") private long executionTimeMs;
    @Column(nullable=false) private boolean success=true;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @PrePersist public void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getActor(){return actor;} public void setActor(String v){actor=v;}
    public String getAction(){return action;} public void setAction(String v){action=v;}
    public String getEntityType(){return entityType;} public void setEntityType(String v){entityType=v;}
    public Long getEntityId(){return entityId;} public void setEntityId(Long v){entityId=v;}
    public String getDetails(){return details;} public void setDetails(String v){details=v;}
    public long getExecutionTimeMs(){return executionTimeMs;} public void setExecutionTimeMs(long v){executionTimeMs=v;}
    public boolean isSuccess(){return success;} public void setSuccess(boolean v){success=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
