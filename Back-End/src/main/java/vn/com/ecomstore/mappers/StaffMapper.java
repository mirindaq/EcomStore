package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import vn.com.ecomstore.dtos.response.staff.StaffResponse;
import vn.com.ecomstore.entities.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    StaffResponse toResponse(Staff staff);
}
