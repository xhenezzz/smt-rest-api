package aidyn.kelbetov.smtrestapi.service;

import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserInfo;
import aidyn.kelbetov.smtrestapi.model.WorkLog;
import aidyn.kelbetov.smtrestapi.repository.DepartmentRepository;
import aidyn.kelbetov.smtrestapi.repository.UserRepository;
import aidyn.kelbetov.smtrestapi.repository.WorkLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class WorkLogService {

    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserService userService;

    public WorkLog startWorkSession(Long userId) {
        User myUser = userRepository.findById(userId).orElseThrow();
        Optional<WorkLog> existingSession = workLogRepository.findByUserIdAndEndTimeIsNull(userId);
        if (existingSession.isPresent()) {
            throw new RuntimeException("Рабочий день уже начат!");
        }

        WorkLog workLog = new WorkLog();
        workLog.setUser(myUser);
        workLog.setStartTime(LocalDateTime.now());
        return workLogRepository.save(workLog);
    }

    public WorkLog stopWorkSession(Long userId) {
        Optional<WorkLog> activeSession = workLogRepository.findByUserIdAndEndTimeIsNull(userId);
        if (activeSession.isEmpty()) {
            throw new RuntimeException("Рабочий день еще не начат!");
        }

        WorkLog workLog = activeSession.get();
        workLog.setEndTime(LocalDateTime.now());

        return workLogRepository.save(workLog);
    }

    public List<WorkLog> getAllWorkSession(Long user) {
        return workLogRepository.findByUserId(user);
    }

    public WorkLog resumeWorkSession(Long user) {
        Optional<WorkLog> existingSession = workLogRepository.findByUserIdAndEndTimeIsNull(user);
        if (existingSession.isPresent()) {
            throw new RuntimeException("Рабочий сеанс уже начат!");
        }

        WorkLog lastSession = workLogRepository.findFirstByUserIdOrderByStartTimeDesc(user);
        if (lastSession == null || lastSession.getEndTime() != null) {
            throw new RuntimeException("Нет активного сеанса работы для продолжения!");
        }

        lastSession.setEndTime(null);
        return workLogRepository.save(lastSession);
    }



    public UserInfo getUserInformationByRange(Long user, LocalDateTime start, LocalDateTime end) {
        List<WorkLog> workLogs = workLogRepository.findByUserIdAndStartTimeBetween(user, start, end);
        long totalWorkTimeMillis = calculateTotalWorkTimeMillis(workLogs);
        long totalWorkTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(totalWorkTimeMillis);
        double totalWorkTimeHours = TimeUnit.MILLISECONDS.toHours(totalWorkTimeMillis);
        User myUser = userRepository.findById(user).orElseThrow();

        UserInfo userInfo = new UserInfo();
        userInfo.setUser(myUser);
        userInfo.setGetUserWorkedList(workLogs);
        userInfo.setTotalWorkTimeHours(totalWorkTimeHours);
        userInfo.setTotalWorkTimeMinutes(totalWorkTimeMinutes);
        return userInfo;
    }

    private long calculateTotalWorkTimeMillis(List<WorkLog> workLogs) {
        long totalWorkTimeMillis = 0;
        for (WorkLog workLog : workLogs) {
            long startTimeEpochSecond = workLog.getStartTime().toEpochSecond(ZoneOffset.UTC);
            long endTimeEpochSecond = workLog.getEndTime().toEpochSecond(ZoneOffset.UTC);
            totalWorkTimeMillis += (endTimeEpochSecond - startTimeEpochSecond) * 1000; // Преобразуем секунды в миллисекунды
        }
        return totalWorkTimeMillis;
    }

}

