package vn.com.ecomstore.dtos.response.customer;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String avatar;
    private boolean active;
    private LocalDate registerDate;
    private LocalDate dateOfBirth;
    private List<String> roles;

}
