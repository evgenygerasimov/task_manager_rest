package com.evgenygerasimov.task_manager_rest.repository;

import com.evgenygerasimov.task_manager_rest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
