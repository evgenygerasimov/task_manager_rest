package com.evgenygerasimov.task_manager_rest.controller;

import com.evgenygerasimov.task_manager_rest.dto.UserDTO;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.service.RoleService;
import com.evgenygerasimov.task_manager_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "This endpoint is used to manage users.")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;


    @Operation(
            summary = "Get all users",
            description = "This endpoint is used to get all users. " +
                    "This endpoint returns a list of user objects in view JSON format." +
                    "This endpoint can be used by any user without any authentication.")
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> showAllUsers() {
        return ResponseEntity.ok(userService.findAllUsersDTO());
    }

    @Operation(
            summary = "Create a new user",
            description = "This endpoint is used to create a new user. " +
                    "The user's password will be encrypted before saving it to the database." +
                    "The user's role will be set to the role (ROLE_EXECUTOR) " +
                    "if the user is a regular user or to the role (ROLE_ADMIN) if the user is an admin." +
                    "            The user object have to be provided in the request body in view JSON format." +
                    "            The data has been in the format: {" +
                    "                \"username\": \"yourusername\"," +
                    "                \"password\": \"yourPassword\"," +
                    "                \"role\": \"ROLE_EXECUTOR\" or \"ROLE_ADMIN\"" +
                    "            }. The username has to be unique. You can use only little letters in the username.")
    @PostMapping("/addNewUser")
    public ResponseEntity<UserDTO> addNewUser(
            @RequestBody @Valid @Parameter(
                    description = "The user object to provide in the request body in view JSON format.",
                    required = true) User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok(userService.convertUserToUserDTO(user));
    }
}
