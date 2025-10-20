package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.role.RoleResponse;
import vn.com.ecomstore.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role role);

}

