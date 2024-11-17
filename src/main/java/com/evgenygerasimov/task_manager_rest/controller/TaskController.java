package com.evgenygerasimov.task_manager_rest.controller;

import com.evgenygerasimov.task_manager_rest.entity.Task;
import com.evgenygerasimov.task_manager_rest.security.JwtService;
import com.evgenygerasimov.task_manager_rest.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtService jwtService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> showAllTasks(@RequestHeader("Authorization") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addNewTask")
    public ResponseEntity<Task> addNewTask(@RequestBody Task task, @RequestHeader("Authorization") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.saveTask(task);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @PutMapping("/updateTask")
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask, @RequestHeader("Authorization") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(taskService.updateTask(updatedTask));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id, @RequestHeader("Authorization") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EXECUTOR')")
    @GetMapping("/userTasks/{userName}")
    public ResponseEntity<List<Task>> showUserTasks(@PathVariable("userName") String userName, @RequestHeader("Authorization") String token) {
        if (!jwtService.getAccessToken(token).isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(taskService.findAllTasksByUser(userName));
    }
}

