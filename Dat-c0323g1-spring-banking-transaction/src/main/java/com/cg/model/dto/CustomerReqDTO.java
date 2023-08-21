package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.dto.LocationRegionReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Valid;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerReqDTO implements Validator {

    private String fullName;
    private String email;
    private String phone;

    @Valid
    private LocationRegionReqDTO locationRegionReqDTO;

    public Customer toCustomer(Long id, BigDecimal balance) {
        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setLocationRegion(locationRegionReqDTO.toLocationRegion(null))
                .setBalance(balance);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        CustomerReqDTO customerCreateReqDTO = (CustomerReqDTO) target;

        String email = customerCreateReqDTO.email;
        String phone = customerCreateReqDTO.phone;


        if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            errors.rejectValue("email", "email.match", "Email chưa đúng định dạng");
        }


        if (!phone.matches("\\d+")) {
            errors.rejectValue("phone", "phone.match", "Số điện thoại phải là ký tự số");
        }

    }
}