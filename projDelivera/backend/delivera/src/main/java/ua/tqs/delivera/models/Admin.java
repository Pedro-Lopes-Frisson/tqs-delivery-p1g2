package ua.tqs.delivera.models;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import javax.persistence.*;

@Entity
@Table(name = "`delivera_admin`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "`user_id`")
  long id;
  
  @Column(name = "`name`")
  String name;
  
  @Column(name = "`password`")
  @Size(min=8)
  String password;
  
  @Column(name = "`email`")
  @Email
  String email;
  
}