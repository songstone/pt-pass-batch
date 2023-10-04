package com.song.pass.domain.pass;

import com.song.pass.domain.constant.BulkPassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkPassRepository extends JpaRepository<BulkPass, Integer> {

    List<BulkPass> findByStatusAndStartedAtGreaterThan(BulkPassStatus status, LocalDateTime localDateTime);
}
