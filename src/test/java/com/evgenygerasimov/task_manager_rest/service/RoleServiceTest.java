package com.evgenygerasimov.task_manager_rest.service;

import com.evgenygerasimov.task_manager_rest.entity.Role;
import com.evgenygerasimov.task_manager_rest.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveRole() {
        //setup
        Role role = new Role();
        role.setAuthority("ROLE_USER");
        //execute
        roleService.save(role);
        //asset
        verify(roleRepository).save(role);
    }
}