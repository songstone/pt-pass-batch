package com.song.pass.domain.pass;

import com.song.pass.domain.BaseEntity;
import com.song.pass.domain.constant.BulkPassStatus;
import com.song.pass.domain.constant.PassStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Entity
@Table(name = "bulk_pass")
public class BulkPass extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bulkPassSeq;
    private Integer packageSeq;
    private String userGroupId;

    @Enumerated(EnumType.STRING)
    private BulkPassStatus status;
    private Integer count;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public Pass toPass(String userId) {
        return Pass.builder()
            .userId(userId)
            .packageSeq(packageSeq)
            .status(PassStatus.READY)
            .remainingCount(count)
            .startedAt(startedAt)
            .endedAt(endedAt)
            .build();
    }
}
