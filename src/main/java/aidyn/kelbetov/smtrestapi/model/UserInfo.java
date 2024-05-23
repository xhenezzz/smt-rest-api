package aidyn.kelbetov.smtrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private User user;
    private List<WorkLog> getUserWorkedList;
    private long totalWorkTimeMinutes;
    private double totalWorkTimeHours;
}
