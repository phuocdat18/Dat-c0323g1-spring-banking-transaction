package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.LocationRegion;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.CustomerReqDTO;
import com.cg.model.dto.CustomerResDTO;
import com.cg.repository.ICustomerRepository;
import com.cg.repository.IDepositRepository;
import com.cg.repository.ILocationRegionRepository;
import com.cg.service.locationRegion.ILocationRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private ILocationRegionService locationRegionService;

    @Autowired
    private IDepositRepository depositRepository;


    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<CustomerResDTO> findAllCustomerResDTO(Boolean deleted) {
        return customerRepository.findAllCustomerResDTO(deleted);
    }

    @Override
    public Customer create(Customer customer) {
        LocationRegion locationRegion = customer.getLocationRegion();
        locationRegionService.save(locationRegion);

        customer.setLocationRegion(locationRegion);
        customer.setBalance(BigDecimal.ZERO);
        customerRepository.save(customer);

        return customer;
    }

    @Override
    public void deposit(Deposit deposit) {
        depositRepository.save(deposit);

        Long customerId = deposit.getCustomer().getId();
        BigDecimal transactionAmount = deposit.getTransactionAmount();
        customerRepository.incrementBalance(customerId, transactionAmount);
    }

    @Override
    public Optional<CustomerDTO> findCustomerDTOById(Long id) {
        return customerRepository.findCustomerDTOById(id);
    }

    @Override
    public CustomerResDTO saveUpdatedCustomerFromDTO(CustomerReqDTO customerReqDTO, CustomerDTO customerDTO) {
        LocationRegion locationRegion = customerReqDTO.getLocationRegionReqDTO().toLocationRegion(null);

        locationRegion.setId(customerDTO.getLocationRegionDTO().getId());

        locationRegionService.save(locationRegion);

        Customer newCustomer = customerReqDTO.toCustomer(customerDTO.getId(), customerDTO.getBalance());

        newCustomer.setLocationRegion(locationRegion);

        return save(newCustomer).toCustomerResDTO();
    }

    @Override
    public Customer save(Customer customer) {
        return null;
    }

    @Override
    public void delete(Customer customer) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
