package com.crm.service;

import com.crm.dto.CustomerCreateDto;
import com.crm.dto.CustomerResponseDto;
import com.crm.dto.CustomerUpdateDto;
import com.crm.model.enums.CustomerStatus;
import com.crm.model.enums.CustomerType;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerCreateDto dto);

    CustomerResponseDto getCustomerById(Long id);

    List<CustomerResponseDto> getAllCustomers(CustomerType type, CustomerStatus status);

    CustomerResponseDto updateCustomer(Long id, CustomerUpdateDto dto);

    void deleteCustomer(Long id);
}
