package com.globaltrade.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="shipments")
public class Shipment implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="shipment_number", nullable=false, unique=true, length=50) private String shipmentNumber;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="vendor_id") private Vendor vendor;
    @Column(name="carrier_name", length=120) private String carrierName;
    @Column(name="tracking_reference", length=100) private String trackingReference;
    @Column(name="origin_country", nullable=false, length=80) private String originCountry;
    @Column(name="destination_country", nullable=false, length=80) private String destinationCountry;
    @Column(name="departure_date") private LocalDateTime departureDate;
    @Column(name="expected_arrival_date") private LocalDateTime expectedArrivalDate;
    @Column(name="actual_arrival_date") private LocalDateTime actualArrivalDate;
    @Column(nullable=false, length=30) private String status="CREATED";
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="created_by") private User createdBy;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @Column(name="updated_at", nullable=false) private LocalDateTime updatedAt;
    @PrePersist public void prePersist(){LocalDateTime n=LocalDateTime.now(); if(createdAt==null)createdAt=n; updatedAt=n;}
    @PreUpdate public void preUpdate(){updatedAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getShipmentNumber(){return shipmentNumber;} public void setShipmentNumber(String v){shipmentNumber=v;}
    public Vendor getVendor(){return vendor;} public void setVendor(Vendor v){vendor=v;}
    public String getCarrierName(){return carrierName;} public void setCarrierName(String v){carrierName=v;}
    public String getTrackingReference(){return trackingReference;} public void setTrackingReference(String v){trackingReference=v;}
    public String getOriginCountry(){return originCountry;} public void setOriginCountry(String v){originCountry=v;}
    public String getDestinationCountry(){return destinationCountry;} public void setDestinationCountry(String v){destinationCountry=v;}
    public LocalDateTime getDepartureDate(){return departureDate;} public void setDepartureDate(LocalDateTime v){departureDate=v;}
    public LocalDateTime getExpectedArrivalDate(){return expectedArrivalDate;} public void setExpectedArrivalDate(LocalDateTime v){expectedArrivalDate=v;}
    public LocalDateTime getActualArrivalDate(){return actualArrivalDate;} public void setActualArrivalDate(LocalDateTime v){actualArrivalDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public User getCreatedBy(){return createdBy;} public void setCreatedBy(User v){createdBy=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
