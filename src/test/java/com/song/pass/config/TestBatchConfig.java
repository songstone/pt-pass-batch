package com.song.pass.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableBatchProcessing
@EntityScan("com.song.pass.domain")
@EnableJpaRepositories("com.song.pass.domain")
@EnableTransactionManagement
public class TestBatchConfig {
}
