package com.crm.service;

import com.crm.dto.CustomerCreateDto;
import com.crm.dto.CustomerResponseDto;
import com.crm.dto.CustomerUpdateDto;
import com.crm.exception.DuplicateResourceException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Customer;
import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import com.crm.repository.CustomerRepository;
import com.crm.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerCreateDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new CustomerCreateDto(
                "Acme Corp",
                "Acme",
                "info@acme.com",
                "+123456789",
                CustomerType.B2B,
                CustomerStatus.ACTIVE,
                Set.of("partner", "tech")
        );

        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .companyName("Acme")
                .email("info@acme.com")
                .phone("+123456789")
                .type(CustomerType.B2B)
                .status(CustomerStatus.ACTIVE)
                .tags(Set.of("partner", "tech"))
                .deals(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should successfully create a customer")
    void createCustomer_Success() {
        when(customerRepository.existsByEmail(createDto.email())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDto response = customerService.createCustomer(createDto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Acme Corp");
        assertThat(response.email()).isEqualTo("info@acme.com");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists during creation")
    void createCustomer_DuplicateEmail_ThrowsException() {
        when(customerRepository.existsByEmail(createDto.email())).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(createDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get customer by ID successfully")
    void getCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDto response = customerService.getCustomerById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("info@acme.com");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when customer ID does not exist")
    void getCustomerById_NotFound_ThrowsException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found with id: 99");
    }

    @Test
    @DisplayName("Should list all customers")
    void getAllCustomers_Success() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponseDto> list = customerService.getAllCustomers(null, null);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("Acme Corp");
    }

    @Test
    @DisplayName("Should update existing customer profile")
    void updateCustomer_Success() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto(
                "Updated Acme",
                null,
                null,
                null,
                null,
                CustomerStatus.VIP,
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDto response = customerService.updateCustomer(1L, updateDto);

        assertThat(response).isNotNull();
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void deleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }
}
