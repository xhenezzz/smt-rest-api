package aidyn.kelbetov.smtrestapi.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_logs")
@Data @AllArgsConstructor @NoArgsConstructor
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime pausedTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
