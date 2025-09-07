package vn.com.ecomstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.ecomstore.enums.WorkStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "staffs")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Staff extends User {

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "work_status")
    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus; // Enum: ACTIVE, INACTIVE, PROBATION

    @Column(name = "employee_code", unique = true)
    private String employeeCode;


}
