package vn.com.ecomstore.services.impl;

import  vn.com.ecomstore.dtos.request.staff.StaffAddRequest;
import  vn.com.ecomstore.dtos.request.staff.StaffUpdateRequest;
import  vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import  vn.com.ecomstore.dtos.response.staff.StaffResponse;
import  vn.com.ecomstore.entities.Role;
import  vn.com.ecomstore.entities.Staff;
import  vn.com.ecomstore.entities.UserRole;
import  vn.com.ecomstore.exceptions.custom.ConflictException;
import  vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import  vn.com.ecomstore.mappers.StaffMapper;
import  vn.com.ecomstore.repositories.RoleRepository;
import  vn.com.ecomstore.repositories.StaffRepository;
import  vn.com.ecomstore.services.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final RoleRepository roleRepository;
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    @Override
    @Transactional
    public StaffResponse createStaff(StaffAddRequest staffAddRequest) {
        if (staffRepository.existsByEmail(staffAddRequest.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        Staff staff = mapAddRequestToStaff(staffAddRequest);

        staffRepository.save(staff);

        return staffMapper.toResponse(staff);

    }

    @Override
    public ResponseWithPagination<List<StaffResponse>> getStaffs(
            int page,
            int size,
            String staffName,
            String email,
            String phone,
            Boolean status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size);

        Page<Staff> staffPage = staffRepository.findAllWithFilters(
                (staffName != null && !staffName.isBlank()) ? staffName : null,
                (email != null && !email.isBlank()) ? email : null,
                (phone != null && !phone.isBlank()) ? phone : null,
                status,
                startDate,
                endDate,
                pageable
        );

        return ResponseWithPagination.fromPage(staffPage, staffMapper::toResponse);
    }


    @Override
    public StaffResponse getStaffById(Long id) {
        return staffMapper.toResponse(getStaffEntityById(id));
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(StaffUpdateRequest staffUpdateRequest, Long id) {
        Staff staff = getStaffEntityById(id);
        mapUpdateRequestToStaff(staffUpdateRequest, staff);
        staffRepository.save(staff);
        return staffMapper.toResponse(staff);
    }

    @Override
    public void changeActive(Long id) {
        Staff staff = getStaffEntityById(id);
        staff.setActive(!staff.getActive());
        staffRepository.save(staff);
    }

    private Staff getStaffEntityById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
    }


    private Staff mapAddRequestToStaff(StaffAddRequest staffAddRequest) {
        Staff staff = Staff.builder()
                .address(staffAddRequest.getAddress())
                .avatar(staffAddRequest.getAvatar())
                .email(staffAddRequest.getEmail())
                .fullName(staffAddRequest.getFullName())
                .password(staffAddRequest.getPassword())
                .phone(staffAddRequest.getPhone())
                .dateOfBirth(staffAddRequest.getDateOfBirth())
                .active(staffAddRequest.isActive())
                .joinDate(staffAddRequest.getJoinDate())
                .workStatus(staffAddRequest.getWorkStatus())
                .build();

        List<UserRole> userRoles = mapRoleIdsToUserRoles(staffAddRequest.getRoleIds(), staff);
        staff.setUserRole(userRoles);

        return staff;
    }

    private void mapUpdateRequestToStaff(StaffUpdateRequest staffUpdateRequest, Staff staff) {
        staff.setAddress(staffUpdateRequest.getAddress());
        staff.setAvatar(staffUpdateRequest.getAvatar());
        staff.setFullName(staffUpdateRequest.getFullName());
        staff.setPhone(staffUpdateRequest.getPhone());
        staff.setDateOfBirth(staffUpdateRequest.getDateOfBirth());
        staff.setJoinDate(staffUpdateRequest.getJoinDate());
        staff.setWorkStatus(staffUpdateRequest.getWorkStatus());

        // Map roles mới nếu có
        if (staffUpdateRequest.getRoleIds() != null) {
            List<UserRole> newUserRoles = mapRoleIdsToUserRoles(staffUpdateRequest.getRoleIds(), staff);
            staff.getUserRole().clear();

            staff.getUserRole().addAll(newUserRoles);
        }
    }


    private List<UserRole> mapRoleIdsToUserRoles(List<Long> roleIds, Staff staff) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        return roles.stream()
                .map(role -> {
                    UserRole ur = new UserRole();
                    ur.setRole(role);
                    ur.setUser(staff); // gán staff để mapping 2 chiều
                    return ur;
                })
                .collect(Collectors.toList());
    }


}