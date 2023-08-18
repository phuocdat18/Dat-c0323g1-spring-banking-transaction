package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.CustomerResDTO;
import com.cg.model.dto.DepositReqDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerApi {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {

        List<Customer> customers = customerService.findAll();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResDTO> getById(@PathVariable Long id) {

        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer newCustomer = customerService.create(customer);

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

//    @PostMapping("/deposit")
//    public ResponseEntity<Customer> deposit(@RequestBody Deposit deposit) {
//
//        customerService.deposit(deposit);
//        Optional<Customer> updateCustomer = customerService.findById(deposit.getCustomer().getId());
//
//        return new ResponseEntity<>(updateCustomer.get(), HttpStatus.OK);
//    }

    @PatchMapping("/deposits/{customerId}")
    public ResponseEntity<?> deposit(@PathVariable String customerId, @RequestBody DepositReqDTO depositReqDTO, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(customerId)) {
            throw new DataInputException("Mã khách hàng nộp tiền không hợp lệ");
        }
        new DepositReqDTO().validate(depositReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        Long id = Long.parseLong(customerId);

        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Mã khách hàng nộp tiền không tồn tại");
        }
        Customer customer = customerOptional.get();


        BigDecimal newBalance = customer.getBalance().add(depositReqDTO.getTransactionAmount());

        if(newBalance.toString().length() > 12) {
            throw new DataInputException("Vượt quá định mức cho phép. Tổng tiền gửi nhỏ hơn 13 số");
        }

        customer.setBalance(newBalance);

        customerService.save(customer);
        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        Deposit deposit = depositReqDTO.toDeposit(null, customerResDTO);
        depositService.save(deposit);

        return new ResponseEntity<>(deposit, HttpStatus.OK);
    }
}
