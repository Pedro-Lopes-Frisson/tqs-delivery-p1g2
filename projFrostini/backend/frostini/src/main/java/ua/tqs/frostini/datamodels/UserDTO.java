package ua.tqs.frostini.datamodels;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @NotNull(message = "Username is mandatory")
  @NotEmpty
  private String name;

  @NotNull(message = "Password is mandatory")
  @Size(min = 8)
  private String pwd;

  @NotNull(message = "Email is mandatory")
  @Email
  private String email;


}
