package aidyn.kelbetov.smtrestapi.Controllers;

import aidyn.kelbetov.smtrestapi.model.Department;
import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserDTO;
import aidyn.kelbetov.smtrestapi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dep")
public class DepartmentController {
    @Autowired
    DepartmentService service;
    @PostMapping("/add")
    public String createDepartment(@RequestBody Department department){
        service.createDepartment(department);
        return "department create success!";
    }

    @GetMapping("/{departmentId}/users")
    public ResponseEntity<List<User>> getUsersInDepartment(@PathVariable Long departmentId) {
        List<User> users = service.getUsersInDepartment(departmentId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{departmentId}/totalWorkHours")
    public String getTotalWorkHoursInDepartmentForPeriod(
            @PathVariable Long departmentId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return service.getTotalWorkHoursInDepartmentForPeriod(departmentId, startDate, endDate);
    }

    @PatchMapping("/update/{depId}")
    public ResponseEntity<String> updateDepTime(@PathVariable Long depId, @RequestBody Department newDepTime){
        service.updateDepartmentTime(depId, newDepTime);
        return ResponseEntity.ok("Пароль для юзера: " + depId + " успешно обновлен!");
    }

    @PatchMapping("/update/{userId}/{depId}")
    public ResponseEntity<String> updateDepForUser(@PathVariable Long userId, @PathVariable Long depId){
        service.updateUserDep(userId, depId);
        return ResponseEntity.ok("Пароль для юзера: " + depId + " успешно обновлен!");
    }

    @GetMapping("/{departmentId}/totalLateCount")
    public int getTotalLateCountForDepartment(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getTotalLateCountForDepartment(departmentId, from, to);
    }

    @GetMapping("/{departmentId}/totalLateTime")
    public String getTotalLateTimeForDepartment(
            @PathVariable Long departmentId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(value = "userId", required = false) Long userId) {
        return service.getTotalLateTimeForDepartment(departmentId, from, to, userId);
    }
}
