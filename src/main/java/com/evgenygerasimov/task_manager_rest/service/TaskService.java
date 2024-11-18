package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.entity.Task;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.exception.TaskNotFoundException;
import com.evgenygerasimov.task_manager_rest.exception.UserNotFoundException;
import com.evgenygerasimov.task_manager_rest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTasksByExecutor(User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        List<Task> allTasks = getAllTasks();
        List<Task> filteredTasks = new ArrayList<>();
        if (user.getRole().equals("ROLE_EXECUTOR")) {
            for (Task task : allTasks) {
                if (task.getExecutor().equals(user.getUsername())) {
                    filteredTasks.add(task);
                }
            }
        }
        return filteredTasks;
    }

    public void saveTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        task.setAuthor(authentication.getName());
        task.setUser(userService.findByUsername(authentication.getName()));
        taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
        }
        throw new TaskNotFoundException("Task not found");
    }

    public void deleteTask(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        if (user.getTasks().contains(getTaskById(id))) {
            taskRepository.deleteById(id);
        } else
            throw new TaskNotFoundException("Task what you tried to delete is not found in your tasks list");
    }

    public List<Task> findAllTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        List<Task> tasks = new ArrayList<>();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            tasks = user.getTasks();
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            tasks = findAllTasksByExecutor(user);
        }
        return tasks;
    }

    public Task updateTask(Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        Task updatedTask = getTaskById(task.getId());
        if (user.getTasks().contains(getTaskById(task.getId())) &&
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setStatus(task.getStatus());
            updatedTask.setPriority(task.getPriority());
            updatedTask.setExecutor(task.getExecutor());
            updatedTask.setComment(task.getComment());
        } else if (findAllTasksByExecutor(user).contains(updatedTask) &&
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            updatedTask.setStatus(task.getStatus());
            updatedTask.setComment(task.getComment());
        } else {
            throw new TaskNotFoundException("Task what you tried to update is not found in your tasks list");
        }
        taskRepository.save(updatedTask);
        return updatedTask;
    }

    public List<Task> findAllTasksByUser(String userName) {
        User user = userService.findByUsername(userName);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        List<Task> tasks = List.of();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            tasks = user.getTasks();
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXECUTOR"))) {
            tasks = findAllTasksByExecutor(user);
        }
        return tasks;
    }
}
