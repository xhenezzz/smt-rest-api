package aidyn.kelbetov.smtrestapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "employee")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String secondName;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "Id", referencedColumnName = "Id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "role_id") })
    private Set<Role> roles;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    private Department userDepartment;
    private int lateCount;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime lateTime;

    public void addLateMinutes(long minutes) {
        if (this.lateTime == null) {
            this.lateTime = LocalTime.MIDNIGHT;
        }
        this.lateTime = this.lateTime.plusMinutes(minutes);
    }
}
