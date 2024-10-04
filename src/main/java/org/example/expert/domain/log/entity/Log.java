package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ip;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "message")
    private String resultMessage;

    private Log(String ip, LocalDateTime createdAt, String resultMessage) {

        this.ip = ip;
        this.createdAt = createdAt;
        this.resultMessage = resultMessage;
    }

    public static Log of(String ip, LocalDateTime createdAt, String resultMessage) {
        return new Log(ip, createdAt, resultMessage);
    }
}
