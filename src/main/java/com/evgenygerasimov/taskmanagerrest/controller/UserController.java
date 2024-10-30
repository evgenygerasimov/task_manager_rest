package com.evgenygerasimov.taskmanagerrest.controller;

import com.evgenygerasimov.taskmanagerrest.entity.Role;
import com.evgenygerasimov.taskmanagerrest.entity.User;
import com.evgenygerasimov.taskmanagerrest.service.RoleService;
import com.evgenygerasimov.taskmanagerrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleService roleService, TaskController taskController) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return ResponseEntity.ok("You are logged in");
        } else return ResponseEntity.ok("You are not logged in");
    }

    @PostMapping("/login")
    public void login(@RequestBody String username, @RequestBody String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> showAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        user.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setUsername(user.getUsername());
        role.setAuthority(user.getRole());
        userService.save(user);
        roleService.save(role);
        return ResponseEntity.ok(user);
    }
}
