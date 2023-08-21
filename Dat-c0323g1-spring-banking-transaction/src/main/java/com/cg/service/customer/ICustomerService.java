package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.CustomerReqDTO;
import com.cg.model.dto.CustomerResDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICustomerService extends IGeneralService<Customer, Long> {

    List<CustomerResDTO> findAllCustomerResDTO(Boolean deleted);

    Customer create(Customer customer);

    void deposit(Deposit deposit);

    Optional<CustomerDTO> findCustomerDTOById(Long id);

    CustomerResDTO saveUpdatedCustomerFromDTO(CustomerReqDTO customerReqDTO, CustomerDTO customerDTO);
}
