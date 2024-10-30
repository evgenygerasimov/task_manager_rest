package com.evgenygerasimov.taskmanagerrest.service;

import com.evgenygerasimov.taskmanagerrest.entity.Task;
import com.evgenygerasimov.taskmanagerrest.entity.User;
import com.evgenygerasimov.taskmanagerrest.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTasksByExecutor(User user) {
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
        taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
        }
        return null;
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }
}
