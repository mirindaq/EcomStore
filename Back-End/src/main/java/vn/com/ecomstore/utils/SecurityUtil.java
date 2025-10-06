package vn.com.ecomstore.utils;

import vn.com.ecomstore.entities.Customer;
import vn.com.ecomstore.entities.Staff;
import vn.com.ecomstore.entities.User;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + userDetails.getUsername()));
        }
        throw new AccessDeniedException("Access denied. You don't have the required role.");
    }

    public Staff getCurrentStaff() {
        User user = getCurrentUser();
        if (!(user instanceof Staff staff)) {
            throw new ResourceNotFoundException("Current user is not a Staff. User ID: " + user.getId());
        }
        return staff;
    }

    public Customer getCurrentCustomer() {
        User user = getCurrentUser();
        if (!(user instanceof Customer customer)) {
            throw new ResourceNotFoundException("Current user is not a Customer. User ID: " + user.getId());
        }
        return customer;
    }

}



