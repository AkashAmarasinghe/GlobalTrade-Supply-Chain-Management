package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="customs_documents")
public class CustomsDocument implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="shipment_id", nullable=false) private Shipment shipment;
    @Column(name="document_type", nullable=false, length=80) private String documentType;
    @Column(name="document_number", length=100) private String documentNumber;
    @Column(nullable=false, length=30) private String status="PENDING";
    @Column(name="due_date") private LocalDateTime dueDate;
    @Column(name="submitted_at") private LocalDateTime submittedAt;
    @Column(name="approved_at") private LocalDateTime approvedAt;
    @Column(length=500) private String notes;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @PrePersist public void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Shipment getShipment(){return shipment;} public void setShipment(Shipment v){shipment=v;}
    public String getDocumentType(){return documentType;} public void setDocumentType(String v){documentType=v;}
    public String getDocumentNumber(){return documentNumber;} public void setDocumentNumber(String v){documentNumber=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getDueDate(){return dueDate;} public void setDueDate(LocalDateTime v){dueDate=v;}
    public LocalDateTime getSubmittedAt(){return submittedAt;} public void setSubmittedAt(LocalDateTime v){submittedAt=v;}
    public LocalDateTime getApprovedAt(){return approvedAt;} public void setApprovedAt(LocalDateTime v){approvedAt=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
