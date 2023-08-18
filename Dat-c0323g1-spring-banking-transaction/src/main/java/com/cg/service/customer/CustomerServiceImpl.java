package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.LocationRegion;
import com.cg.repository.ICustomerRepository;
import com.cg.repository.IDepositRepository;
import com.cg.repository.ILocationRegionRepository;
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
    private ILocationRegionRepository locationRegionRepository;

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
    public Customer create(Customer customer) {
        LocationRegion locationRegion = customer.getLocationRegion();
        locationRegionRepository.save(locationRegion);

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

//        BigDecimal currentBalance = customer.getBalance();

//        BigDecimal newBalance = currentBalance.add(transactionAmount);
//        customer.setBalance(currentBalance);
//        customerRepository.save(customer);

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
