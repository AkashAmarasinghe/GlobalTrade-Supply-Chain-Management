package com.globaltrade.remote;
import com.globaltrade.exception.CarrierIntegrationException;
import jakarta.ejb.Remote;
@Remote public interface CarrierGatewayRemote {
    String fetchShipmentStatus(String trackingReference) throws CarrierIntegrationException;
}
