package com.song.pass.config.job.pass;

import com.song.pass.domain.constant.PassStatus;
import com.song.pass.domain.pass.Pass;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class ExpirePassesJobConfig {

    private final JobBuilderFactory jbf;
    private final StepBuilderFactory sbf;
    private final EntityManagerFactory emf;

    private static final int CHUNK_SIZE = 5;

    @Bean
    public Job expirePassesJob() {
        return jbf.get("expirePassesJob")
            .start(expirePassesStep())
            .build();
    }

    @Bean
    public Step expirePassesStep() {
        return sbf.get("expirePassesStep")
            .<Pass, Pass>chunk(CHUNK_SIZE)
            .reader(expirePassesItemReader())
            .processor(expirePassesItemProcessor())
            .writer(expirePassesItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<Pass> expirePassesItemReader() {
        return new JpaCursorItemReaderBuilder<Pass>()
            .name("expiresPassesItemReader")
            .entityManagerFactory(emf)
            .queryString("select p from Pass p where p.status = :status and p.endedAt <= :endedAt")
            .parameterValues(Map.of("status", PassStatus.IN_PROGRESS, "endedAt", LocalDateTime.now()))
            .build();
    }

    @Bean
    public ItemProcessor<Pass, Pass> expirePassesItemProcessor() {
        return pass -> {
            pass.setStatus(PassStatus.EXPIRED);
            pass.setExpiredAt(LocalDateTime.now());
            return pass;
        };
    }

    @Bean
    public JpaItemWriter<Pass> expirePassesItemWriter() {
        return new JpaItemWriterBuilder<Pass>()
            .entityManagerFactory(emf)
            .build();
    }

}
