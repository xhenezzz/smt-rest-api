package aidyn.kelbetov.smtrestapi.service;

import aidyn.kelbetov.smtrestapi.model.Department;
import aidyn.kelbetov.smtrestapi.model.Role;
import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserDTO;
import aidyn.kelbetov.smtrestapi.repository.DepartmentRepository;
import aidyn.kelbetov.smtrestapi.repository.RoleRepository;
import aidyn.kelbetov.smtrestapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public UserDTO addUser(User user){
        Optional<Department> department = departmentRepository.findById(user.getUserDepartment().getId());
        if(department.isPresent()){
            user.setUserDepartment(department.get());
        } else {
            throw new RuntimeException("Департамент не найден!");
        }
        if (user.getRoles() == null) {
            throw new RuntimeException("Роли пользователя не определены!");
        }
        Set<Role> rolesSet = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role foundRole = roleRepository.findByName(role.getName());
            if (foundRole == null) {
                throw new RuntimeException("Роль с именем " + role.getName() + " не найдена!");
            }
            rolesSet.add(foundRole);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(rolesSet);
        User savedUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(savedUser.getFirstName());
        userDTO.setSecondName(savedUser.getSecondName());
        userDTO.setDepartment(savedUser.getUserDepartment());
        userDTO.setRoles(savedUser.getRoles());
        return userDTO;
    }

    public void updateUserPassword(Long userId, User newPassword){
        User user = userRepository.findById(userId).orElseThrow();
        String encodeNewPass = passwordEncoder.encode(newPassword.getPassword());
        user.setPassword(encodeNewPass);
        userRepository.save(user);
    }


    public List<Role> getAllRoles(){return roleRepository.findAll();}


    public Role createRole(String name){
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }


    public boolean isUserLate(LocalTime startTime, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Department department = user.getUserDepartment();

            if (department != null) {
                LocalTime startDepTimeForUser = department.getStartTime();

                if (startTime.isAfter(startDepTimeForUser)) {
                    long lateMinutes = startTime.until(startDepTimeForUser, ChronoUnit.MINUTES);

                    // Обновляем общее время опозданий
                    user.setLateTime(user.getLateTime().plusMinutes(lateMinutes));

                    // Увеличиваем счетчик опозданий
                    user.setLateCount(user.getLateCount() + 1);

                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


}