package com.globaltrade.bean;
import com.globaltrade.exception.CarrierIntegrationException;
import com.globaltrade.remote.CarrierGatewayRemote;
import jakarta.ejb.Stateless;
@Stateless public class CarrierGatewayBean implements CarrierGatewayRemote {
    @Override public String fetchShipmentStatus(String trackingReference) {
        if(trackingReference==null||trackingReference.isBlank()) throw new CarrierIntegrationException("Carrier tracking reference is missing");
        if(trackingReference.toUpperCase().startsWith("FAIL")) throw new CarrierIntegrationException("Simulated carrier system outage");
        return "IN_TRANSIT";
    }
}
