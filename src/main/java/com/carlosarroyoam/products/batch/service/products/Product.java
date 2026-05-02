package com.carlosarroyoam.products.batch.service.products;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
  private String index;
  private String title;
  private String description;
  private String brand;
  private String category;
  private BigDecimal price;
  private String currency;
  private Integer stock;
  private String ean;
  private String color;
  private String size;
  private String availability;
  private String internalId;

  @Override
  public String toString() {
    return "Product [title=" + title + ", description=" + description + ", brand=" + brand
        + ", category=" + category + ", price=" + price + ", currency=" + currency + ", stock=" + stock + ", ean=" + ean
        + ", color=" + color + ", size=" + size + ", availability=" + availability + "]";
  }
}
