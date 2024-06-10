package aidyn.kelbetov.smtrestapi.Controllers;
import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserInfo;
import aidyn.kelbetov.smtrestapi.model.WorkLog;
import aidyn.kelbetov.smtrestapi.repository.UserRepository;
import aidyn.kelbetov.smtrestapi.service.DepartmentService;
import aidyn.kelbetov.smtrestapi.service.UserService;
import aidyn.kelbetov.smtrestapi.service.WorkLogService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private WorkLogService workLogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    @PostMapping("/start")
    public ResponseEntity<WorkLog> startWorkSession(@RequestParam Long userId){
        WorkLog workLog = workLogService.startWorkSession(userId);
        userService.isUserLate(workLog.getStartTime().toLocalTime(), userId);
        return ResponseEntity.ok(workLog);
    }

    @PostMapping("/stop")
    public WorkLog stopWorkSession(@RequestParam Long userId) {
        return workLogService.stopWorkSession(userId);
    }

    @PostMapping("/resume")
    public WorkLog resumeWorkSession(@RequestParam Long userId) {
        return workLogService.resumeWorkSession(userId);
    }
    @GetMapping("/user/{userId}")
    public List<WorkLog> getUserWorkSessions(@RequestParam Long userId) {
        return workLogService.getAllWorkSession(userId);
    }
    @GetMapping("/range")
    public ResponseEntity<UserInfo> getInformationByRange(@RequestParam Long userId, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end){
        UserInfo jsonResponse = workLogService.getUserInformationByRange(userId, start, end);
        return ResponseEntity.ok(jsonResponse);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<String> updateUserPass(@PathVariable Long userId, @RequestBody User newPassword){
        userService.updateUserPassword(userId, newPassword);
        return ResponseEntity.ok("Пароль для юзера: " + userId + " успешно обновлен!");
    }

    @GetMapping("/getSalary/{userId}")
    public ResponseEntity<BigDecimal> getSalary(@PathVariable Long userId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        User user = userRepository.findById(userId).orElseThrow();
        List<WorkLog> workLogs = workLogService.getAllWorkSession(user.getId());

        if (startDate == null && endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now();
        }

        BigDecimal salary = departmentService.calculateSalary(user, workLogs, startDate, endDate);

        return ResponseEntity.ok(salary);
    }
}
