package com.cg.model.dto;


import com.cg.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DepositReqDTO implements Validator {
    private BigDecimal transactionAmount;

    public Deposit toDeposit(Long id, CustomerResDTO customerResDTO) {
        return new Deposit()
                .setId(id)
                .setCustomer(customerResDTO.toCustomer())
                .setTransactionAmount(transactionAmount)
                ;

    }
    @Override
    public boolean supports(Class<?> clazz) {
        return DepositReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepositReqDTO depositReqDTO = (DepositReqDTO) target;

        BigDecimal transactionAmount = depositReqDTO.transactionAmount;


        if (transactionAmount == null) {
            errors.rejectValue("transactionAmount", "transactionAmount.null", "Tiền gửi là bắt buộc");
        } else {
            if (transactionAmount.toString().trim().length() == 0) {
                errors.rejectValue("transactionAmount", "transactionAmount.empty", "Tiền gửi là không được để trống");
            } else {
                if (!transactionAmount.toString().matches("\\d+")) {
                    errors.rejectValue("transactionAmount", "transactionAmount.match", "Tiền gửi phải là ký tự số");
                }
                if (!Pattern.matches("\\b([1-9]\\d{2,11}|999999999999)\\b", transactionAmount.toString())) {
                    errors.rejectValue("transactionAmount", "transactionAmount.length.min-max", "Tiền gửi phải lớn hơn 100 và nhỏ hơn 13 số");
                }
            }
        }
    }
}
