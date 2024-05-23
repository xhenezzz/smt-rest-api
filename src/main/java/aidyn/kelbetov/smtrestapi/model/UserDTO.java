package aidyn.kelbetov.smtrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstName;
    private String secondName;
    private Department department;
    private Set<Role> roles;
    private int lateCount;
    private LocalTime lateTime;
}
