package com.evgenygerasimov.task_manager_rest.controller;

import com.evgenygerasimov.task_manager_rest.dto.UserDTO;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.service.RoleService;
import com.evgenygerasimov.task_manager_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> showAllUsers() {
        return ResponseEntity.ok(userService.findAllUsersDTO());
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<UserDTO> addNewUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok(userService.convertUserToUserDTO(user));
    }
}
