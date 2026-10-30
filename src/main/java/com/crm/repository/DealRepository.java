package com.crm.repository;

import com.crm.model.Deal;
import com.crm.model.enums.DealStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByCustomerId(Long customerId);

    List<Deal> findByStage(DealStage stage);

    List<Deal> findByAssignedUserId(Long userId);
}
