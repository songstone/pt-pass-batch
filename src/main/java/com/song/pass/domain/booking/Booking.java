package com.song.pass.domain.booking;

import com.song.pass.domain.BaseEntity;
import com.song.pass.domain.Notification.Notification;
import com.song.pass.domain.constant.BookingStatus;
import com.song.pass.domain.constant.NotificationEvent;
import com.song.pass.domain.user.User;
import com.song.pass.util.LocalDateTimeUtil;
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
@Table(name = "booking")
public class Booking extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingSeq;
    private Integer passSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private boolean usedPass;
    private boolean attended;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime cancelledAt;

    public Notification toNotificationEntity() {
        return Notification.builder()
            .uuid(user.getUuid())
            .event(NotificationEvent.BEFORE_CLASS)
            .text(String.format(
                "안녕하세요. %s 수업 싲가합니다. 수업 전 출석 체크 바랍니다. \uD83D\uDE0A", LocalDateTimeUtil.format(startedAt)
            ))
            .build();
    }
}
