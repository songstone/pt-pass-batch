package com.song.pass.domain.Notification;

import com.song.pass.domain.constant.NotificationEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "notification")
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationSeq;
    private String uuid;

    @Enumerated(EnumType.STRING)
    private NotificationEvent event;
    private String text;
    private boolean sent;
    private LocalDateTime sentAt;

    @Builder
    public Notification(String uuid, NotificationEvent event, String text) {
        this.uuid = uuid;
        this.event = event;
        this.text = text;
    }
}
