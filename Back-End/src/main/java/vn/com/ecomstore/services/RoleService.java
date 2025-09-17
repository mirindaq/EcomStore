package vn.com.ecomstore.services;


import  vn.com.ecomstore.dtos.response.role.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAllRoles();
}
