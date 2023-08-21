package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.CustomerResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query("UPDATE Customer AS cus " +
            "SET cus.balance = cus.balance + :transactionAmount " +
            "WHERE cus.id = :customerId"
    )
    void incrementBalance(@Param("customerId") Long customerId, @Param("transactionAmount") BigDecimal transactionAmount);

    @Query("SELECT NEW com.cg.model.dto.CustomerResDTO (" +
            "cus.id, " +
            "cus.fullName, " +
            "cus.email, " +
            "cus.phone, " +
            "cus.balance, " +
            "cus.locationRegion" +
            ") FROM Customer AS cus " +
            "WHERE cus.deleted = :deleted"
    )
    List<CustomerResDTO> findAllCustomerResDTO(@Param("deleted") Boolean deleted);

    @Query("SELECT NEW com.cg.model.dto.CustomerResDTO (" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.balance, " +
            "c.locationRegion " +
            ") " +
            "FROM Customer c " +
            "WHERE c.id = :id AND c.deleted = false")
    Optional<CustomerDTO> findCustomerDTOById(@Param("id") Long id);
}
