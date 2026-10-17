package com.globaltrade.dto;

import com.globaltrade.entity.SystemAlert;

public class AlertView {
    private Long id;
    private String severity;
    private String category;
    private String title;
    private String message;
    private String referenceType;
    private Long referenceId;
    private String createdAt;

    public AlertView() {}

    public AlertView(SystemAlert alert) {
        this.id = alert.getId();
        this.severity = alert.getSeverity();
        this.category = alert.getCategory();
        this.title = alert.getTitle();
        this.message = alert.getMessage();
        this.referenceType = alert.getReferenceType();
        this.referenceId = alert.getReferenceId();
        this.createdAt = alert.getCreatedAt() == null ? null : alert.getCreatedAt().toString();
    }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getSeverity(){return severity;} public void setSeverity(String v){severity=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public String getReferenceType(){return referenceType;} public void setReferenceType(String v){referenceType=v;}
    public Long getReferenceId(){return referenceId;} public void setReferenceId(Long v){referenceId=v;}
    public String getCreatedAt(){return createdAt;} public void setCreatedAt(String v){createdAt=v;}
}
