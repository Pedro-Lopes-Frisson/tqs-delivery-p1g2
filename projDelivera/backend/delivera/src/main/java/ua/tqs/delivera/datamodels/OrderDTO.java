package ua.tqs.delivera.datamodels;

import java.io.Serializable;

import ua.tqs.delivera.models.Store;
public class OrderDTO implements Serializable {
    private Long externalId;
    private String clientLocation;
    private Store store;
    private Long orderMadeTimestamp;

    public OrderDTO(Long externalId, String clientLocation, Store store, Long orderMadeTimestamp) {
        this.externalId = externalId;
        this.clientLocation = clientLocation;
        this.store = store;
        this.orderMadeTimestamp = orderMadeTimestamp;
    }
    
    public Long getExternalId() { return externalId; }

    public String getClientLocation() { return clientLocation; }

    public Store getStore() { return store; }

    public Long getOrderMadeTimestamp() { return orderMadeTimestamp; }
}
