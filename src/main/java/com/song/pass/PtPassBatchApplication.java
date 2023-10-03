package com.song.pass;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@EnableBatchProcessing // 배치 실행 설정
@SpringBootApplication
public class PtPassBatchApplication {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step passStep() {
        return stepBuilderFactory.get("passStep")
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Execute passStep");
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Job passJob() {
        return jobBuilderFactory.get("passJob")
            .start(passStep())
            .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PtPassBatchApplication.class, args);
    }

}
