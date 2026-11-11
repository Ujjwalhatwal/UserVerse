package com.ujjwal.UserManagamentApp.controller;

import com.ujjwal.UserManagamentApp.dto.ChangePasswordDTO;
import com.ujjwal.UserManagamentApp.dto.UserDTO;
import com.ujjwal.UserManagamentApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<UserDTO> changePassword(@PathVariable Long userId, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        // Implement password change logic here
        UserDTO userDTO = userService.changePassword(userId, changePasswordDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO) {
        // Implement user update logic here
        return ResponseEntity.ok(userService.updateUser(userId, userDTO));
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        // Implement user deletion logic here
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successful");
    }

}
