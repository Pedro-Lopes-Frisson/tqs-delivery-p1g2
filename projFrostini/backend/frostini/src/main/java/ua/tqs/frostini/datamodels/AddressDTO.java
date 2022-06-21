package ua.tqs.frostini.datamodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
   
  @NotNull
  private long userId;
  
  @NotNull
  private double latitude;

  @NotNull
  private double longitude;
}
