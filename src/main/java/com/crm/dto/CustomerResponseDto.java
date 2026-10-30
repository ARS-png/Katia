package com.crm.dto;

import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "Response DTO containing full customer details")
public record CustomerResponseDto(
        Long id,
        String name,
        String companyName,
        String email,
        String phone,
        CustomerType type,
        CustomerStatus status,
        Set<String> tags,
        List<DealSummaryDto> deals,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
