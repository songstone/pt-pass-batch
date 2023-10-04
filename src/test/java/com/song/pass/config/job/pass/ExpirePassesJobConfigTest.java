package com.song.pass.config.job.pass;

import com.song.pass.config.TestBatchConfig;
import com.song.pass.domain.constant.PassStatus;
import com.song.pass.domain.pass.Pass;
import com.song.pass.domain.pass.PassRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
class ExpirePassesJobConfigTest {

    private final JobLauncherTestUtils jobLauncherTestUtils;

    private final PassRepository passRepository;

    public ExpirePassesJobConfigTest(
        @Autowired JobLauncherTestUtils jobLauncherTestUtils,
        @Autowired PassRepository passRepository
    ) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.passRepository = passRepository;
    }

    @Test
    void expirePassesStep() throws Exception {
        //given
        addPasses(10);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        //then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobInstance.getJobName()).isEqualTo("expirePassesJob");
    }

    private void addPasses(int size) {
        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();

        List<Pass> passes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Pass pass = Pass.builder()
                .packageSeq(1)
                .userId("A" + 1000000 + i)
                .status(PassStatus.IN_PROGRESS)
                .remainingCount(random.nextInt(11))
                .startedAt(now.minusDays(60))
                .endedAt(now.minusDays(1))
                .build();
            passes.add(pass);
        }

        passRepository.saveAll(passes);
    }

}