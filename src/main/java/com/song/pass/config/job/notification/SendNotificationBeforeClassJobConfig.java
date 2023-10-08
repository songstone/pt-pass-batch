package com.song.pass.config.job.notification;

import com.song.pass.domain.Notification.Notification;
import com.song.pass.domain.booking.Booking;
import com.song.pass.domain.constant.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SendNotificationBeforeClassJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jbf;
    private final StepBuilderFactory sbf;
    private final EntityManagerFactory emf;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    @Bean
    public Job sendNotificationBeforeClassJob() {
        return jbf.get("sendNotificationBeforeClassJob")
            .start(addNotificationStep())
            .next(sendNotificationStep())
            .build();
    }

    @Bean
    public Step addNotificationStep() {
        return sbf.get("addNotificationStep")
            .<Booking, Notification>chunk(CHUNK_SIZE)
            .reader(addNotificationItemReader())
            .processor(addNotificationItemProcessor())
            .writer(addNotificationItemWriter())
            .build();
    }

    /**
     * 스레드 세이프 하다, 조회한 데이터 들에 업데이트가 이루어지지 않으므로 cursor 말고 paging 사용,
     */
    @Bean
    public JpaPagingItemReader<Booking> addNotificationItemReader() {
        return new JpaPagingItemReaderBuilder<Booking>()
            .name("addNotificationItemReader")
            .entityManagerFactory(emf)
            .pageSize(CHUNK_SIZE)
            .queryString("select b from Booking b join fetch b.user where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")
            .build();
    }

    @Bean
    public ItemProcessor<Booking, Notification> addNotificationItemProcessor() {
        return Booking::toNotificationEntity;
    }

    @Bean
    public ItemWriter<Notification> addNotificationItemWriter() {
        return new JpaItemWriterBuilder<Notification>()
            .entityManagerFactory(emf)
            .build();
    }

    @Bean
    public Step sendNotificationStep() {
        return sbf.get("sendNotificationStep")
            .<Notification, Notification>chunk(CHUNK_SIZE)
            .reader(sendNotificationItemReader())
            .writer(sendNotificationItemWriter)
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    /**
     * JPACursor 방식은 스레드 세이프 하지 않으므로 스레드 세이프 하도록 처리
     */
    @Bean
    public SynchronizedItemStreamReader<Notification> sendNotificationItemReader() {
        JpaCursorItemReader<Notification> itemReader = new JpaCursorItemReaderBuilder<Notification>()
            .name("sendNotificationItemReader")
            .entityManagerFactory(emf)
            .queryString("select n from Notification n where n.event = :event and n.sent :sent")
            .parameterValues(Map.of("event", NotificationEvent.BEFORE_CLASS, "sent", false))
            .build();

        return new SynchronizedItemStreamReaderBuilder<Notification>()
            .delegate(itemReader)
            .build();
    }
}
