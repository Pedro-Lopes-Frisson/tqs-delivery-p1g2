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
  @NotEmpty
  private long userId;
  
  @NotNull
  @NotEmpty
  private String street;

  @NotNull
  @NotEmpty
  private String city;
  
  @NotNull
  @NotEmpty
  private String zipCode;
}
