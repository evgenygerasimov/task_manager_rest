package com.evgenygerasimov.task_manager_rest.controller;

import com.evgenygerasimov.task_manager_rest.entity.Task;
import com.evgenygerasimov.task_manager_rest.config.security.JwtService;
import com.evgenygerasimov.task_manager_rest.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Task management", description = "This API is used to manage tasks.")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtService jwtService;

    @Operation(
            summary = "Get all user's tasks",
            description = "This endpoint is used to get all own tasks. " +
                    "Only users with the 'ROLE_ADMIN' or 'ROLE_EXECUTOR' authority can access this endpoint.")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> showAllTasks(@RequestHeader("Authorization") @Parameter(
            description = "The JWT token have to be passed in the request header.",
            required = true) String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @Operation(
            summary = "Add a new task",
            description = "This endpoint is used to add a new task. Only users with the 'ROLE_ADMIN' " +
                    "authority can access this endpoint." +
                    "The task object have to be provided in the request body in view JSON format: {\n" +
                    "        \"title\": \"yor task title\"," +
                    "        \"description\": \"your task description\"," +
                    "        \"status\": \"your task status\"," +
                    "        \"priority\": \"your task priority\"," +
                    "        \"executor\": \"the name of the task executor\"," +
                    "        \"comment\": \"yor task comment\"" +
                    "    }")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addNewTask")
    public ResponseEntity<Task> addNewTask(@RequestBody @Valid Task task, @RequestHeader("Authorization") @Parameter
            (description = "The JWT token have to be passed in the request header.") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.saveTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "Update a task",
            description = "This endpoint is used to update a task. Only users with the 'ROLE_ADMIN' or " +
                    "'ROLE_EXECUTOR' authority can access this endpoint." +
                    "The task object have to be provided in the request body in view JSON format: {\n" +
                    "        \"id\": 7," +
                    "        \"title\": \"your new task title\"," +
                    "        \"description\": \" your new task description\"," +
                    "        \"status\": \" your new task status\"," +
                    "        \"priority\": \" your new task priority\"," +
                    "        \"executor\": \" the name of the new task executor\"," +
                    "        \"comment\": \"your new task comment\"" +
                    "    } The user with the 'ROLE_ADMIN' authority can update all fields of the task, " +
                    "while the user with the 'ROLE_EXECUTOR' authority can only update the 'comment' " +
                    "field and the 'status' field. And users can update only his own tasks.")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @PutMapping("/updateTask")
    public ResponseEntity<Task> updateTask(
            @RequestBody @Valid Task updatedTask, @RequestHeader("Authorization") @Parameter
            (description = "The JWT token have to be passed in the request header.") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(taskService.updateTask(updatedTask));
    }

    @Operation(
            summary = "Delete a task",
            description = "This endpoint is used to delete a task by its id. " +
                    "Only users with the 'ROLE_ADMIN' authority can access this endpoint.")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable("id") @Parameter(description = "The id of the task to be deleted.",
                    required = true) int id, @RequestHeader("Authorization")
            @Parameter(description = "The JWT token have to be passed in the request header.") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Get all users tasks by user name",
            description = "This endpoint is used to get all tasks of a user by its name. " +
                    "Only users with the 'ROLE_ADMIN' or" +
                    " 'ROLE_EXECUTOR' authority can access this endpoint.")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @GetMapping("/userTasks/{userName}")
    public ResponseEntity<List<Task>> showUserTasks(
            @PathVariable("userName") @Parameter(description = "The name of the user whose tasks will be shown.",
                    required = true) String userName, @RequestHeader("Authorization")
            @Parameter(description = "The JWT token have to be passed in the request header.") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(taskService.findAllTasksByUser(userName));
    }
}

