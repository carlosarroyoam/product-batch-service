package com.carlosarroyoam.products.batch.service.core.config;

import com.carlosarroyoam.products.batch.service.products.Product;
import com.carlosarroyoam.products.batch.service.products.ProductItemProcessor;
import com.carlosarroyoam.products.batch.service.products.ProductJobExecutionListener;
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
  Job csvToDbJob(
      JobRepository jobRepository, Step step, ProductJobExecutionListener jobExecutionListener) {
    return new JobBuilder("csvToDbJob", jobRepository)
        .listener(jobExecutionListener)
        .start(step)
        .build();
  }

  @Bean
  Step step(
      JobRepository jobRepository,
      DataSourceTransactionManager transactionManager,
      FlatFileItemReader<Product> reader,
      ProductItemProcessor processor,
      JdbcBatchItemWriter<Product> writer) {
    return new StepBuilder("step1", jobRepository)
        .<Product, Product>chunk(100, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  FlatFileItemReader<Product> reader() {
    return new FlatFileItemReaderBuilder<Product>()
        .name("productItemReader")
        .resource(new ClassPathResource("csv/products.csv"))
        .linesToSkip(1)
        .delimited()
        .delimiter(",")
        .quoteCharacter('"')
        .names(
            "index",
            "title",
            "description",
            "brand",
            "category",
            "price",
            "currency",
            "stock",
            "ean",
            "color",
            "size",
            "availability",
            "internalId")
        .targetType(Product.class)
        .build();
  }

  @Bean
  JdbcBatchItemWriter<Product> writer(DataSource datasource) {
    return new JdbcBatchItemWriterBuilder<Product>()
        .sql(
            """
			INSERT INTO products (title, description, brand, category, price, currency, stock, ean, color, size, availability)
			VALUES (:title, :description, :brand, :category, :price, :currency, :stock, :ean, :color, :size, :availability)
			""")
        .dataSource(datasource)
        .beanMapped()
        .build();
  }
}
