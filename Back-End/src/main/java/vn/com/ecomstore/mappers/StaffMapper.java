package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.staff.StaffResponse;
import vn.com.ecomstore.entities.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    StaffResponse toResponse(Staff staff);
}
