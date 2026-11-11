package com.ujjwal.UserManagamentApp.controller;

import com.ujjwal.UserManagamentApp.dto.LoginRequestDTO;
import com.ujjwal.UserManagamentApp.dto.LoginResponseDTO;
import com.ujjwal.UserManagamentApp.dto.RegisterRequestDTO;
import com.ujjwal.UserManagamentApp.dto.UserDTO;
import com.ujjwal.UserManagamentApp.service.AuthenticationService;
import com.ujjwal.UserManagamentApp.service.UserService;
import com.ujjwal.UserManagamentApp.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerNormalUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        UserDTO userDTO = authenticationService.registerNormalUser(registerRequestDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO  = authenticationService.loginUser(loginRequestDTO);
        ResponseCookie cookie = ResponseCookie.from("JWT",loginResponseDTO.getJwtToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60) // 1 hour
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDTO.getUserDTO());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(){
        return authenticationService.logoutUser();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if(authentication == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Authenticated");
        }
        String username = authentication.getName();
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }


}
