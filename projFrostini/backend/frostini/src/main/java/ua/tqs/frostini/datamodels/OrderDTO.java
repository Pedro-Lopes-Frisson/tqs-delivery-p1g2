package ua.tqs.frostini.datamodels;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
  @NotNull
  long addressId;
  @NotNull
  long userId;
  
  @NotNull
  @NotEmpty
  List<@Valid OrderedProductDTO> orderedProductsList; //validate each product
}
