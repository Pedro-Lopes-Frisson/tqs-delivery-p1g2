package ua.tqs.frostini.datamodels;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductDTO {
  @Min( 1 )
  @NotNull
  long quantity;
  
  @NotNull
  long productId;
  
  
}
