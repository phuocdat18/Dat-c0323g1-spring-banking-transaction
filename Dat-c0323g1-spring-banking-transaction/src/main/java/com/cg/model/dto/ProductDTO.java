package com.cg.model.dto;

import com.cg.model.Category;
import com.cg.utils.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotEmpty;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO implements Validator {

    @NotEmpty
    private String title;
    private String price;
    private String unit;

    private Category category;

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDTO productDTO = (ProductDTO) o;

        String title = productDTO.title;
        String unit = productDTO.unit;
        String priceStr = productDTO.price;

        if (title.length() == 0) {
            errors.rejectValue("title", "title.length");
        }
        else {
            if (title.length() < 5 || title.length() > 20) {
                errors.rejectValue("title", "title.minMaxLength");
            }
        }

        if (unit.length() == 0) {
            errors.rejectValue("unit", "unit.length");
        }

        if (!ValidateUtils.isNumberValid(priceStr)) {
            errors.rejectValue("price", "price.matches");
        }
        else {
            long price = Long.parseLong(priceStr);

            if (price < 0) {
                errors.rejectValue("price", "price.min");
            }
            if (price > 10000) {
                errors.rejectValue("price", "price.max");
            }
        }
    }
}
