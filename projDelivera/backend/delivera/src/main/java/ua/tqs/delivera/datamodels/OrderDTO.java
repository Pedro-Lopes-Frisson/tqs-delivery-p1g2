package ua.tqs.delivera.datamodels;

import ua.tqs.delivera.models.Store;

public class OrderDTO {
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
    
    //public Long getExternalId() {}
}
