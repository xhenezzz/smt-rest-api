package aidyn.kelbetov.smtrestapi.repository;

import aidyn.kelbetov.smtrestapi.model.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    Optional<WorkLog> findByUserIdAndEndTimeIsNull(Long Id);
    List<WorkLog> findByUserId(Long Id);

    WorkLog findFirstByUserIdOrderByStartTimeDesc(Long id);
    List<WorkLog> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime from, LocalDateTime to);
}
