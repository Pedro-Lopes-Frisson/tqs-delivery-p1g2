package ua.tqs.frostini.models;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "frostini_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class Address {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  long id;
  
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}


