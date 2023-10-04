package com.song.pass.config.job.pass;

import com.song.pass.domain.constant.BulkPassStatus;
import com.song.pass.domain.constant.PassStatus;
import com.song.pass.domain.pass.BulkPass;
import com.song.pass.domain.pass.BulkPassRepository;
import com.song.pass.domain.pass.Pass;
import com.song.pass.domain.pass.PassRepository;
import com.song.pass.domain.user.UserGroupMapping;
import com.song.pass.domain.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AddPassesTaskletTest {

    @Mock
    private StepContribution stepContribution;

    @Mock
    private ChunkContext chunkContext;

    @Mock
    private PassRepository passRepository;

    @Mock
    private BulkPassRepository bulkPassRepository;

    @Mock
    private UserGroupMappingRepository userGroupMappingRepository;

    @InjectMocks
    private AddPassesTasklet addPassesTasklet;

    @Test
    void execute() throws Exception {
        //given
        String userGroupId = "GROUP";
        String userId = "A1000000";
        Integer packageSeq = 1;
        Integer count = 10;

        LocalDateTime now = LocalDateTime.now();

        BulkPass bulkPass = BulkPass.builder()
            .packageSeq(packageSeq)
            .userGroupId(userGroupId)
            .status(BulkPassStatus.READY)
            .count(count)
            .startedAt(now)
            .endedAt(now.plusDays(60))
            .build();

        UserGroupMapping userGroupMapping = UserGroupMapping.builder()
            .userGroupId(userGroupId)
            .userId(userId)
            .build();

        //when
        when(bulkPassRepository.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any())).thenReturn(List.of(bulkPass));
        when(userGroupMappingRepository.findByUserGroupId(eq("GROUP"))).thenReturn(List.of(userGroupMapping));

        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);

        //then
        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);

        // 로직 중 세부 갑 확인 용도로 사용
        ArgumentCaptor<List> passesCaptor = ArgumentCaptor.forClass(List.class);
        verify(passRepository, times(1)).saveAll(passesCaptor.capture());
        List<Pass> passes = passesCaptor.getValue();

        assertThat(passes.size()).isEqualTo(1);

        Pass pass = passes.get(0);
        assertThat(pass.getPackageSeq()).isEqualTo(packageSeq);
        assertThat(pass.getUserId()).isEqualTo(userId);
        assertThat(pass.getStatus()).isEqualTo(PassStatus.READY);
        assertThat(pass.getRemainingCount()).isEqualTo(count);
    }
}