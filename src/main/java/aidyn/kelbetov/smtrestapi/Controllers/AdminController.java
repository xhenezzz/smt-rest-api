package aidyn.kelbetov.smtrestapi.Controllers;

import aidyn.kelbetov.smtrestapi.config.MyUserDetailsService;
import aidyn.kelbetov.smtrestapi.model.Role;
import aidyn.kelbetov.smtrestapi.model.User;
import aidyn.kelbetov.smtrestapi.model.UserDTO;
import aidyn.kelbetov.smtrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService service;

    @Autowired
    MyUserDetailsService userService;
    @PostMapping("/register")
    public String createUser(@RequestBody User user){
        service.addUser(user);
        return "user is saved";
    }

    @GetMapping("/all")
    public List<User> allUsers(){
        return service.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            service.createRole(role.getName());
            return ResponseEntity.ok("Роль успешно создана!!" + role.getName());
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
}
