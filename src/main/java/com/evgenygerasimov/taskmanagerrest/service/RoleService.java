package com.evgenygerasimov.taskmanagerrest.service;

import com.evgenygerasimov.taskmanagerrest.entity.Role;
import com.evgenygerasimov.taskmanagerrest.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    public void save(Role role) {
        roleRepository.save(role);
    }
}
