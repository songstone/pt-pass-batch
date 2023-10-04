package com.song.pass.domain.packaze;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer> {

    List<Package> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE Package p" +
            "       SET p.count = :count," +
            "           p.period = :period" +
            "       WHERE p.packageSeq = :packageSeq"
    )
    int updateCountAndPeriod(
        @Param("packageSeq") Integer packageSeq,
        @Param("count") Integer count,
        @Param("period") Integer period);
}
