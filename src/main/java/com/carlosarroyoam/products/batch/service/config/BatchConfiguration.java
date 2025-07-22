package com.carlosarroyoam.products.batch.service.config;

import com.carlosarroyoam.products.batch.service.listeners.JobCompletionNotificationListener;
import com.carlosarroyoam.products.batch.service.models.Product;
import com.carlosarroyoam.products.batch.service.processors.ProductItemProcessor;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfiguration {
  @Bean
  Job csvToDbJob(JobRepository jobRepository, Step step,
      JobCompletionNotificationListener listener) {
    return new JobBuilder("csvToDbJob", jobRepository).listener(listener).start(step).build();
  }

  @Bean
  Step step(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
      FlatFileItemReader<Product> reader, ProductItemProcessor processor,
      JdbcBatchItemWriter<Product> writer) {
    return new StepBuilder("step1", jobRepository).<Product, Product>chunk(100, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  FlatFileItemReader<Product> reader() {
    return new FlatFileItemReaderBuilder<Product>().name("productItemReader")
        .resource(new ClassPathResource("csv/products.csv"))
        .linesToSkip(1)
        .delimited()
        .names("Index", "Title", "Description", "Brand", "Category", "Price", "Currency", "Stock",
            "EAN", "Color", "Size", "Availability", "Internal ID")
        .targetType(Product.class)
        .build();
  }

  @Bean
  JdbcBatchItemWriter<Product> writer(DataSource datasource) {
    return new JdbcBatchItemWriterBuilder<Product>().sql(
        "INSERT INTO products (title, description, brand, category, price, currency, stock, ean, color, size, availability) VALUES (:title, :description, :brand, :category, :price, :currency, :stock, :ean, :color, :size, :availability)")
        .dataSource(datasource)
        .beanMapped()
        .build();
  }
}
