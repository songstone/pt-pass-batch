package com.song.pass.config.job.notification;

import com.song.pass.adapter.message.KakaoTalkMessageAdapter;
import com.song.pass.domain.Notification.Notification;
import com.song.pass.domain.Notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendNotificationItemWriter implements ItemWriter<Notification> {

   private final NotificationRepository notificationRepository;
   private final KakaoTalkMessageAdapter kakaoTalkMessageAdapter;

   @Override
   public void write(List<? extends Notification> notifications) throws Exception {
       int count = 0;

       for (Notification notification : notifications) {
           boolean successful = kakaoTalkMessageAdapter.sendKakaoTalkMessage(notification.getUuid(), notification.getText());

           if (successful) {
               notification.setSent(true);
               notification.setSentAt(LocalDateTime.now());
               notificationRepository.save(notification);
               count++;
           }

       }
       log.info("SendNotificationItemWriter - write: 수업 전 알람 {}/{}건 전송 성공", count, notifications.size());

   }
}
