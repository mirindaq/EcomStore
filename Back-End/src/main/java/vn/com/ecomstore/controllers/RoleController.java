package vn.com.ecomstore.controllers;

import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.role.RoleResponse;
import vn.com.ecomstore.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/for-admin")
    public ResponseEntity<ResponseSuccess<List<RoleResponse>>> getAllRolesForAdmin() {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get roles success",
                roleService.getAllRolesForAdmin()
        ));
    }
}
