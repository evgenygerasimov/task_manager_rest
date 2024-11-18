package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.entity.Task;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.exception.TaskNotFoundException;
import com.evgenygerasimov.task_manager_rest.exception.UserNotFoundException;
import com.evgenygerasimov.task_manager_rest.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setRole("ROLE_EXECUTOR");

        task = new Task();
        task.setId(1);
        task.setExecutor("testUser");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnAllTasksTest() {
        //setup
        when(taskRepository.findAll()).thenReturn(List.of(task));
        //execute
        List<Task> tasks = taskService.getAllTasks();
        //assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void shouldFindAllTasksByExecutor() {
        //setup
        when(taskRepository.findAll()).thenReturn(List.of(task));
        //execute
        List<Task> tasks = taskService.findAllTasksByExecutor(user);
        //assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task.getExecutor(), tasks.get(0).getExecutor());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenFindAllTasksByExecutorUserNotFound() {
        //execute and assert
        assertThrows(UserNotFoundException.class, () -> taskService.findAllTasksByExecutor(null));
    }

    @Test
    void shouldSaveTaskTest() {
        //setup
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        //execute
        taskService.saveTask(task);
        //assert
        assertEquals("testUser", task.getAuthor());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldReturnTaskByIdTest() {
        //setup
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        //execute
        Task foundTask = taskService.getTaskById(1);
        //assert
        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskNotFoundTest() {
        //setup
        when(taskRepository.findById(1)).thenReturn(Optional.empty());
        //execute and assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1));
    }

    @Test
    void shouldThroeTaskNotFoundExceptionWhenDeleteTaskNotFoundTest() {
        //setup
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(taskRepository.findById(1)).thenReturn(Optional.empty());
        //execute and assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenDeleteTaskUserNotFoundTest() {
        //setup
        when(userService.findByUsername("unknownUser")).thenReturn(null);
        //execute and assert
        assertThrows(UserNotFoundException.class, () -> taskService.findAllTasksByUser("unknownUser"));
    }
}
