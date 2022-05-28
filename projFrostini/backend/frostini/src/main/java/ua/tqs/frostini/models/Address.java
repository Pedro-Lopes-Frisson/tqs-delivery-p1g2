package ua.tqs.frostini.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
  
  @ManyToOne
  @JoinColumn(name="id", nullable=false)
  private User user;
}
