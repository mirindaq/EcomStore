package vn.com.ecomstore.dtos.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserProfileResponse {
    private String email;
    private String fullName;
    private List<String> roles;
    private String avatar;
    private String phone;
    private String address;
}
