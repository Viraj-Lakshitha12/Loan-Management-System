package com.test.loan.controller;

import com.test.loan.dto.common.ApiResponse;
import com.test.loan.dto.request.LoanDto;
import com.test.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    @Autowired
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createLoan(@RequestBody LoanDto loanDto) {
        return ResponseEntity.ok(ApiResponse.success(200, "Loan created successfully",
                loanService.createLoan(loanDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Loan found successfully",
                loanService.getLoan(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> changeLoanStatus(@PathVariable Long id, @RequestParam String LoanStatus) {
        return ResponseEntity.ok(ApiResponse.success(200, "Loan status updated successfully",
                loanService.changeLoanStatus(id, LoanStatus)));
    }
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<String>> approveLoan(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Loan approved successfully",
                loanService.ApproveLoan(id)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<String>> rejectLoan(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Loan rejected successfully",
                loanService.RejectLoan(id)));
    }
}
