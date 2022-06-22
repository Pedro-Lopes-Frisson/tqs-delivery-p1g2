package ua.tqs.frostini.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.minidev.json.JSONObject;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDelivera {
  Long id; // delivera
  
  Long externalId; // o que nos mandamos
  
  String clientLocation;
  
  Long orderMadeTimestamp;
  
  Long deliveredTimestamp;
  
  String orderState;
  
}
