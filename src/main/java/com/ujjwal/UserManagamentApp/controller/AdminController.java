package com.ujjwal.UserManagamentApp.controller;

import com.ujjwal.UserManagamentApp.dto.RegisterRequestDTO;
import com.ujjwal.UserManagamentApp.dto.UserDTO;
import com.ujjwal.UserManagamentApp.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AuthenticationService  authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerAdminUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        UserDTO userDTO = authenticationService.registerAdminUser(registerRequestDTO);
        return ResponseEntity.ok(userDTO);
    }


}
