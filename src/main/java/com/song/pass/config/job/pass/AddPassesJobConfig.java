package com.song.pass.config.job.pass;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AddPassesJobConfig {

    private final JobBuilderFactory jbf;
    private final StepBuilderFactory sbf;

    private final AddPassesTasklet addPassesTasklet;

    @Bean
    public Job addPassesJob() {
        return jbf.get("addPassesJob")
            .start(addPassesStep())
            .build();
    }

    @Bean
    public Step addPassesStep() {
        return sbf.get("addPassesStep")
            .tasklet(addPassesTasklet)
            .build();
    }
}
