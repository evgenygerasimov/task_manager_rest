package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.dto.UserDTO;
import com.evgenygerasimov.task_manager_rest.entity.Role;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.exception.UserExistException;
import com.evgenygerasimov.task_manager_rest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setRole("USER");
        user.setEnabled(1);
    }

    @Test
    public void shouldSaveUserSuccessTest() {
        //setup
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        //execute
        userService.save(user);
        //assert
        verify(userRepository, times(1)).save(user);
        verify(roleService, times(1)).save(any(Role.class));
    }

    @Test
    public void shouldThrowExceptionWhenSaveUserUserAlreadyExistsTest() {
        //setup
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        //execute
        assertThrows(UserExistException.class, () -> {
            userService.save(user);
        });
        //assert
        verify(userRepository, never()).save(any(User.class));
        verify(roleService, never()).save(any(Role.class));
    }

    @Test
    public void shouldConvertUsersToUsersDTOTest() {
        //setup
        List<User> users = Arrays.asList(user, user);
        when(userRepository.findAll()).thenReturn(users);
        //execute
        List<UserDTO> usersDTO = userService.findAllUsersDTO();
        //assert
        assertNotNull(usersDTO);
        assertEquals(users.size(), usersDTO.size());
    }

    @Test
    public void shouldReturnAllUsersTest() {
        //setup
        List<User> users = Arrays.asList(user, user);
        when(userRepository.findAll()).thenReturn(users);
        //execute
        List<User> foundUsers = userService.findAll();
        //assert
        assertNotNull(foundUsers);
        assertEquals(users.size(), foundUsers.size());
    }

    @Test
    public void shouldConvertUserToUserDTOTest() {
        //setup and execute
        UserDTO userDTO = userService.convertUserToUserDTO(user);
        //assert
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    public void shouldFindUserByUsernameTest() {
        //setup
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        //execute
        User foundUser = userService.findByUsername(user.getUsername());
        //assert
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void shouldThrowExceptionWhenFindUserByUsernameIsNotFoundTest() {
        //setup
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        //execute and assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.userDetailsService().loadUserByUsername(user.getUsername());
        });
    }

    @Test
    public void shouldReturnUserDetailsWhenFindUserByUsernameTest() {
        //setup
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        //execute
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getUsername());
        //assert
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());

    }
}
