package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.response.role.RoleResponse;
import vn.com.ecomstore.entities.Role;
import vn.com.ecomstore.mappers.RoleMapper;
import vn.com.ecomstore.repositories.RoleRepository;
import vn.com.ecomstore.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRolesForAdmin() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .filter(role -> !"CUSTOMER".equalsIgnoreCase(role.getName()))
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }
}
