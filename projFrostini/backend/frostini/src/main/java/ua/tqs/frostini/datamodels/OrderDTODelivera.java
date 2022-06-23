package ua.tqs.frostini.datamodels;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data @AllArgsConstructor @NoArgsConstructor @Getter @Setter public class OrderDTODelivera {
  @NotEmpty private String storeName; // StoreName
  
  @Positive private Long orderStoreId; // id do order na specific
  // lat and lon da store
  
  @Min(- 90) @Max(90) private double storeLat;
  @Min(- 180) @Max(180) private double storeLon;
  
  // lat and lon do client
  @Min(- 90) @Max(90) private double clientLat;
  @Min(- 180) @Max(180) private double clientLon;
  
  // original price with no transportation
  @Positive private double orderPrice;
  
}
