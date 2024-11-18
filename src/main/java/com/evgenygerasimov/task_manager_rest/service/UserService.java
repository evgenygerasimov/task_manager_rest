package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.dto.UserDTO;
import com.evgenygerasimov.task_manager_rest.entity.Role;
import com.evgenygerasimov.task_manager_rest.entity.User;
import com.evgenygerasimov.task_manager_rest.exception.UserExistException;
import com.evgenygerasimov.task_manager_rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    public void save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserExistException("User with username " + user.getUsername() + " already exist");
        }
        Role role = new Role();
        role.setUsername(user.getUsername());
        role.setAuthority(user.getRole());
        userRepository.save(user);
        roleService.save(role);
    }

    public List<UserDTO> findAllUsersDTO() {
        List<User> users = userRepository.findAll();
        return convertUsersListToUsersDTOList(users);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<UserDTO> convertUsersListToUsersDTOList(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEnabled(user.getEnabled());
            userDTO.setRole(user.getRole());
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                User employee = userRepository.findByUsername(username);
                if (employee == null) {
                    throw new UsernameNotFoundException("User not found with username: " + username);
                }
                return employee;
            }
        };
    }
}
