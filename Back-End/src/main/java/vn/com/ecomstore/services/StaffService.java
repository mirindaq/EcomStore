package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.staff.StaffAddRequest;
import vn.com.ecomstore.dtos.request.staff.StaffUpdateRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.staff.StaffResponse;

import java.time.LocalDate;
import java.util.List;

public interface StaffService {
    StaffResponse createStaff(StaffAddRequest staffAddRequest);

    ResponseWithPagination<List<StaffResponse>> getStaffs(
            int page,
            int size,
            String staffName,
            String email,
            String phone,
            Boolean status,
            LocalDate startDate,
            LocalDate endDate
    );

    StaffResponse getStaffById(Long id);

    StaffResponse updateStaff(StaffUpdateRequest staffUpdateRequest, Long id);

    void changeActive(Long id);
}
