package com.song.pass.config.job.pass;

import com.song.pass.domain.constant.BulkPassStatus;
import com.song.pass.domain.pass.BulkPass;
import com.song.pass.domain.pass.BulkPassRepository;
import com.song.pass.domain.pass.Pass;
import com.song.pass.domain.pass.PassRepository;
import com.song.pass.domain.user.UserGroupMapping;
import com.song.pass.domain.user.UserGroupMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    // 시작 하루전 이용권들을 발급
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        List<BulkPass> bulkPasses = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

        int count = 0;
        for (BulkPass bulkPass : bulkPasses) {
            List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPass.getUserGroupId())
                .stream()
                .map(UserGroupMapping::getUserId)
                .toList();

            count += addPasses(bulkPass, userIds);

            bulkPass.setStatus(BulkPassStatus.COMPLETED);
        }

        log.info("AddPassesTasklet - execute: 이용권 {}건 추가 완료, startedAt={}", count, startedAt);

        return RepeatStatus.FINISHED;
    }

    private int addPasses(BulkPass bulkPass, List<String> userIds) {
        List<Pass> passes = userIds.stream()
            .map(bulkPass::toPass)
            .toList();

        return passRepository.saveAll(passes).size();
    }
}
