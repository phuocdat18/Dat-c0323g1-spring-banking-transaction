package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.service.IGeneralService;

public interface ICustomerService extends IGeneralService<Customer, Long> {

    Customer create(Customer customer);

    void deposit(Deposit deposit);
}
