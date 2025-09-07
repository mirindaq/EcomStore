package vn.com.ecomstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.ecomstore.enums.Gender;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends User {

    @Column(name = "register_date")
    private LocalDate registerDate; // Ngày đăng ký tài khoản

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;



}
