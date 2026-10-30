package com.crm.dto;

import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Data Transfer Object for updating an existing Customer")
public record CustomerUpdateDto(
        @Schema(description = "Full name of contact person or client")
        @Size(max = 150, message = "Name must not exceed 150 characters")
        String name,

        @Schema(description = "Company name if B2B client")
        @Size(max = 150, message = "Company name must not exceed 150 characters")
        String companyName,

        @Schema(description = "Email address")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Schema(description = "Contact phone number")
        @Size(max = 30, message = "Phone must not exceed 30 characters")
        String phone,

        @Schema(description = "Customer type (B2B or B2C)")
        CustomerType type,

        @Schema(description = "Customer status")
        CustomerStatus status,

        @Schema(description = "Custom tags for categorizing customers")
        Set<String> tags
) {}
