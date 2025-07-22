package com.carlosarroyoam.products.batch.service.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
  private static final Logger log = LoggerFactory
      .getLogger(JobCompletionNotificationListener.class);
  private final JdbcTemplate jdbcTemplate;

  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      Integer count = jdbcTemplate.queryForObject("SELECT count(id) FROM products", Integer.class);
      log.info("Job finished");
      log.info("Total inserted records: {}", count);
    }
  }
}