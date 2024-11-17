package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.entity.Role;
import com.evgenygerasimov.task_manager_rest.repository.RoleRepository;
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
