package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import vn.com.ecomstore.dtos.response.role.RoleResponse;
import vn.com.ecomstore.entities.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role role);

}

