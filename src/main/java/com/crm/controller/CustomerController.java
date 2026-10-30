package com.crm.controller;

import com.crm.dto.CustomerCreateDto;
import com.crm.dto.CustomerResponseDto;
import com.crm.dto.CustomerUpdateDto;
import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import com.crm.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Endpoints for managing CRM customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create a new customer profile", description = "Creates a B2B or B2C customer record with contact information and tags.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate email")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerCreateDto dto) {
        CustomerResponseDto created = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieves customer profile by unique ID including associated deal summaries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "List all customers", description = "Retrieves all customers, optionally filtered by customer type or status.")
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(
            @RequestParam(required = false) CustomerType type,
            @RequestParam(required = false) CustomerStatus status
    ) {
        return ResponseEntity.ok(customerService.getAllCustomers(type, status));
    }

    @Operation(summary = "Update an existing customer profile", description = "Updates specified fields of a customer profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDto dto
    ) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @Operation(summary = "Delete customer by ID", description = "Deletes a customer profile and associated records.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
