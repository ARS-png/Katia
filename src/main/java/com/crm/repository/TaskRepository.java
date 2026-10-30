package com.crm.repository;

import com.crm.model.Task;
import com.crm.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedUserId(Long userId);

    List<Task> findByCustomerId(Long customerId);

    List<Task> findByDealId(Long dealId);

    List<Task> findByStatus(TaskStatus status);
}
