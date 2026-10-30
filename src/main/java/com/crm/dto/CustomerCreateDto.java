package com.crm.dto;

import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Data Transfer Object for creating a new Customer")
public record CustomerCreateDto(
        @Schema(description = "Full name of contact person or client", example = "Ivan Ivanov")
        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must not exceed 150 characters")
        String name,

        @Schema(description = "Company name if B2B client", example = "Acme Corp")
        @Size(max = 150, message = "Company name must not exceed 150 characters")
        String companyName,

        @Schema(description = "Email address", example = "ivan@acme.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Schema(description = "Contact phone number", example = "+79991234567")
        @Size(max = 30, message = "Phone must not exceed 30 characters")
        String phone,

        @Schema(description = "Customer type (B2B or B2C)", example = "B2B")
        @NotNull(message = "Customer type is required")
        CustomerType type,

        @Schema(description = "Initial customer status", example = "LEAD")
        @NotNull(message = "Customer status is required")
        CustomerStatus status,

        @Schema(description = "Custom tags for categorizing customers", example = "[\"important\", \"tech\"]")
        Set<String> tags
) {}
