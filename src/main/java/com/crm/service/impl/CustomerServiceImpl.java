package com.crm.service.impl;

import com.crm.dto.CustomerCreateDto;
import com.crm.dto.CustomerResponseDto;
import com.crm.dto.CustomerUpdateDto;
import com.crm.dto.DealSummaryDto;
import com.crm.exception.DuplicateResourceException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Customer;
import com.crm.model.Deal;
import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;
import com.crm.repository.CustomerRepository;
import com.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerCreateDto dto) {
        if (customerRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Customer with email '" + dto.email() + "' already exists");
        }

        Customer customer = Customer.builder()
                .name(dto.name())
                .companyName(dto.companyName())
                .email(dto.email())
                .phone(dto.phone())
                .type(dto.type())
                .status(dto.status())
                .tags(dto.tags() != null ? new HashSet<>(dto.tags()) : new HashSet<>())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = findCustomerEntity(id);
        return mapToResponseDto(customer);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers(CustomerType type, CustomerStatus status) {
        List<Customer> customers;
        if (type != null) {
            customers = customerRepository.findByType(type);
        } else if (status != null) {
            customers = customerRepository.findByStatus(status);
        } else {
            customers = customerRepository.findAll();
        }

        return customers.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerUpdateDto dto) {
        Customer customer = findCustomerEntity(id);

        if (dto.email() != null && !dto.email().equalsIgnoreCase(customer.getEmail())) {
            if (customerRepository.existsByEmail(dto.email())) {
                throw new DuplicateResourceException("Customer with email '" + dto.email() + "' already exists");
            }
            customer.setEmail(dto.email());
        }

        if (dto.name() != null) customer.setName(dto.name());
        if (dto.companyName() != null) customer.setCompanyName(dto.companyName());
        if (dto.phone() != null) customer.setPhone(dto.phone());
        if (dto.type() != null) customer.setType(dto.type());
        if (dto.status() != null) customer.setStatus(dto.status());
        if (dto.tags() != null) customer.setTags(new HashSet<>(dto.tags()));

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = findCustomerEntity(id);
        customerRepository.delete(customer);
    }

    private Customer findCustomerEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    private CustomerResponseDto mapToResponseDto(Customer customer) {
        List<DealSummaryDto> dealDtos = customer.getDeals() != null
                ? customer.getDeals().stream().map(this::mapToDealSummaryDto).toList()
                : Collections.emptyList();

        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getCompanyName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getType(),
                customer.getStatus(),
                customer.getTags(),
                dealDtos,
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    private DealSummaryDto mapToDealSummaryDto(Deal deal) {
        return new DealSummaryDto(
                deal.getId(),
                deal.getTitle(),
                deal.getValue(),
                deal.getStage(),
                deal.getExpectedCloseDate(),
                deal.getCreatedAt()
        );
    }
}
