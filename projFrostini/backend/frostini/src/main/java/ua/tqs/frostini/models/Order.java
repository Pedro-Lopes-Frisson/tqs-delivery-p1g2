package ua.tqs.frostini.models;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name = "frostini_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Order {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  long id;
  
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}

