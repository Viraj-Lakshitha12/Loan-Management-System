package com.test.loan.repo;

import com.test.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepo extends JpaRepository<Loan,Long> {
    boolean existsByLoanNumber(String loanNumber);
}
