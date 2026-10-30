package com.crm.dto;

import com.crm.model.enums.DealStage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Summary representation of a Deal")
public record DealSummaryDto(
        Long id,
        String title,
        BigDecimal value,
        DealStage stage,
        LocalDate expectedCloseDate,
        LocalDateTime createdAt
) {}
