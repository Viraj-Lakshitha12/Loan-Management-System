package com.test.loan.repo;

import com.test.loan.entity.Customer;
import com.test.loan.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    boolean existsCustomerByNic(String nic);
}
