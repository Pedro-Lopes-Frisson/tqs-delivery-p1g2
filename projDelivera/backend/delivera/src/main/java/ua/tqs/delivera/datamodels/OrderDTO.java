package ua.tqs.delivera.datamodels;


import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
  private String storeName; // StoreName
  private Long orderStoreId; // id do order na specific
  // lat and lon da store
  private double storeLat;
  private double storeLon;
  
  // lat and lon do client
  private double clientLat;
  private double clientLon;
  
  // original price with no transportation
  private double orderPrice;
  
}
