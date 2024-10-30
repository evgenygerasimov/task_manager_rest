package com.evgenygerasimov.taskmanagerrest.service;

import com.evgenygerasimov.taskmanagerrest.entity.User;
import com.evgenygerasimov.taskmanagerrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getListExecutors(){
        List<User> listAllUsers = userRepository.findAll();
        List<User> listExecutorUsers = new ArrayList<>();
        for (User user : listAllUsers) {
            if(user.getRole().equals("ROLE_EXECUTOR")) {
                listExecutorUsers.add(user);
            }
        }
        return listExecutorUsers;
    }
}
