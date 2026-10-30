package com.crm.controller;

import com.crm.dto.CustomerCreateDto;
import com.crm.dto.CustomerResponseDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import com.crm.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser
    @DisplayName("POST /api/v1/customers - Should create customer and return HTTP 201")
    void createCustomer_ValidBody_ReturnsCreated() throws Exception {
        CustomerCreateDto createDto = new CustomerCreateDto(
                "John Doe",
                "Doe Corp",
                "john@doe.com",
                "+123456",
                CustomerType.B2C,
                CustomerStatus.LEAD,
                Set.of("new")
        );

        CustomerResponseDto responseDto = new CustomerResponseDto(
                1L,
                "John Doe",
                "Doe Corp",
                "john@doe.com",
                "+123456",
                CustomerType.B2C,
                CustomerStatus.LEAD,
                Set.of("new"),
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/v1/customers - Should return 400 when invalid payload")
    void createCustomer_InvalidBody_ReturnsBadRequest() throws Exception {
        CustomerCreateDto invalidDto = new CustomerCreateDto(
                "",
                "Doe Corp",
                "invalid-email",
                "+123456",
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/customers/{id} - Should return customer details")
    void getCustomerById_Success() throws Exception {
        CustomerResponseDto responseDto = new CustomerResponseDto(
                1L,
                "John Doe",
                "Doe Corp",
                "john@doe.com",
                "+123456",
                CustomerType.B2C,
                CustomerStatus.LEAD,
                Set.of("new"),
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.getCustomerById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/customers/{id} - Should return 404 when not found")
    void getCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new ResourceNotFoundException("Customer not found with id: 99"));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/customers - Should return list of customers")
    void getAllCustomers_Success() throws Exception {
        when(customerService.getAllCustomers(null, null)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
