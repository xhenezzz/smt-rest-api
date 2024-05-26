package aidyn.kelbetov.smtrestapi.service;

import aidyn.kelbetov.smtrestapi.model.Department;
import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserDTO;
import aidyn.kelbetov.smtrestapi.model.WorkLog;
import aidyn.kelbetov.smtrestapi.repository.DepartmentRepository;
import aidyn.kelbetov.smtrestapi.repository.UserRepository;
import aidyn.kelbetov.smtrestapi.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository repository;
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private UserRepository userRepository;
    public void createDepartment(Department d){
        Department department = new Department();
        department.setHourlyRate(d.getHourlyRate());
        department.setName(d.getName());
        department.setStartTime(d.getStartTime());
        repository.save(department);
    }

    public List<User> getUsersInDepartment(Long departmentId){
        Department department = repository.findById(departmentId).orElseThrow();
        List<User> users = department.getUsers();
        return users;
    }


    public String getTotalWorkHoursInDepartmentForPeriod(Long departmentId, LocalDateTime startDate, LocalDateTime endDate) {
        Department department = repository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        List<User> users = department.getUsers();

        Duration totalWorkTime = Duration.ZERO;

        for (User user : users) {
            List<WorkLog> workLogs = workLogRepository.findByUserIdAndStartTimeBetween(user.getId(), startDate, endDate);
            totalWorkTime = totalWorkTime.plus(calculateTotalWorkHours(workLogs));
        }

        long hours = totalWorkTime.toHours();
        long minutes = totalWorkTime.toMinutesPart();

        return String.format("%d:%02d", hours, minutes);
    }


    public Duration calculateTotalWorkHours(List<WorkLog> workLogs) {
        long totalWorkTimeMillis = 0;
        for (WorkLog workLog : workLogs) {
            LocalDateTime startTime = workLog.getStartTime();
            LocalDateTime endTime = workLog.getEndTime();

            long workTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);
            totalWorkTimeMillis += workTimeMillis;
        }
        return Duration.ofMillis(totalWorkTimeMillis);
    }

    public void updateDepartmentTime(Long depId, Department newDepTime){
        Department department = repository.findById(depId).orElseThrow();
        department.setStartTime(newDepTime.getStartTime());
        repository.save(department);
    }


    public void updateUserDep(Long userId, Long dep1){
        User user = userRepository.findById(userId).orElseThrow();
        Department newDepartment = repository.findById(dep1).orElseThrow();
        user.setUserDepartment(newDepartment);
        userRepository.save(user);
    }

    public int getTotalLateCountForDepartment(Long departmentId, LocalDate from, LocalDate to){
        Department department = repository.findById(departmentId).orElseThrow();
        int countTotalLate = 0;
        List<User> users = department.getUsers();
        for (User user : users) {
            countTotalLate+= user.getLateCount();
        }

        return countTotalLate;
    }

    public String getTotalLateTimeForDepartment(Long departmentId, LocalDateTime from, LocalDateTime to, Long userId) {
        Department department = repository.findById(departmentId).orElseThrow();
        long totalLateTime = 0;

        List<User> users = (userId != null) ? List.of(userRepository.findById(userId).orElseThrow()) : department.getUsers();

        for (User user : users) {
            List<WorkLog> workLogs = workLogRepository.findByUserIdAndStartTimeBetween(user.getId(), from, to);
            for (WorkLog workLog : workLogs) {
                LocalDateTime departmentStartTime = LocalDateTime.of(LocalDate.from(from), user.getUserDepartment().getStartTime());
                if (workLog.getStartTime().isAfter(departmentStartTime)) {
                    totalLateTime += Duration.between(departmentStartTime, workLog.getStartTime()).toMinutes();
                }
            }
        }
        long hours = totalLateTime / 60;
        long minutes = totalLateTime % 60;
        return String.format("%d:%02d", hours, minutes);
    }



}
