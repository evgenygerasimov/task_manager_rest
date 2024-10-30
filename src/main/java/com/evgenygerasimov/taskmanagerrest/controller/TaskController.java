package com.evgenygerasimov.taskmanagerrest.controller;

import com.evgenygerasimov.taskmanagerrest.entity.Task;
import com.evgenygerasimov.taskmanagerrest.entity.User;
import com.evgenygerasimov.taskmanagerrest.service.TaskService;
import com.evgenygerasimov.taskmanagerrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> showAllTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        List<Task> tasks = new ArrayList<>();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            tasks = user.getTasks();
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            tasks = taskService.findAllTasksByExecutor(user);
        }
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/addNewTask")
    public String addNewTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        task.setAuthor(authentication.getName());
        task.setUser(userService.findByUsername(authentication.getName()));
        taskService.saveTask(task);
        return "Task added successfully";
    }

    @PutMapping("/updateTask")
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        Task existingTask = taskService.getTaskById(updatedTask.getId());
        if (user.getTasks().contains(taskService.getTaskById(updatedTask.getId())) &&
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setExecutor(updatedTask.getExecutor());
            existingTask.setComment(updatedTask.getComment());
        } else if (taskService.findAllTasksByExecutor(user).contains(existingTask) &&
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setComment(updatedTask.getComment());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        taskService.saveTask(existingTask);
        return ResponseEntity.ok(existingTask);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        int taskId = 0;
        for (Task task : user.getTasks()) {
            if (task.getId() == id) {
                taskId = task.getId();
            }
        }
        taskService.deleteTask(taskId);
        return "Task deleted successfully";
    }

    @GetMapping("/userTasks/{userName}")
    public ResponseEntity<List<Task>> showUserTasks(@PathVariable("userName") String userName) {
        User user = userService.findByUsername(userName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Task> tasks = List.of();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            tasks = user.getTasks();
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            tasks = taskService.findAllTasksByExecutor(user);
        }
        return ResponseEntity.ok(tasks);
    }
}

