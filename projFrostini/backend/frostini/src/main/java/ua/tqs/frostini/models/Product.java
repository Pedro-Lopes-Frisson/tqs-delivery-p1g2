package ua.tqs.frostini.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.tqs.frostini.datamodels.ProductDTO;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "frostini_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  long id;
  
  @Column
  Double price;
  
  @Column(name = "stock_quantity")
  int stockQuantity;
  
  @Column
  String name;
  
  @Column
  String description;
  
  @JsonIgnore
  @OneToMany(mappedBy = "product")
  List<OrderedProduct> orderedProductList;

  public Product(ProductDTO productDTO){
    this.price = productDTO.getPrice();
    this.stockQuantity = productDTO.getStockQuantity();
    this.name = productDTO.getName();
    this.description =productDTO.getDescription();
  }
  
}
