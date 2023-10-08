package com.song.pass.domain.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Transactional
    @Modifying
    @Query( value = "UPDATE Booking b" +
        "               SET b.usedPass = :usedPass," +
        "                   b.modifiedAt = CURRENT_TIMESTAMP " +
        "               WHERE b.passSeq = :passSeq"
    )
    int updateUsedPass(@Param("possSeq") Integer passSeq, @Param("usedPass") boolean usedPass);
}
