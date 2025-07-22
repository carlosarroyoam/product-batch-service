package com.carlosarroyoam.products.batch.service.processors;

import com.carlosarroyoam.products.batch.service.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {
  private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);

  @Override
  public Product process(Product product) throws Exception {
    log.info("Processing product: {}", product);
    return product;
  }
}
