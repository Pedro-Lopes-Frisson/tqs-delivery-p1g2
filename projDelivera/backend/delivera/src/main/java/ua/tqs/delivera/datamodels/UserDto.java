package ua.tqs.delivera.datamodels;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data public class UserDto implements Serializable {
  private final String name;
  @Size(min = 8) private final String password;
  @Email private final String email;
}
