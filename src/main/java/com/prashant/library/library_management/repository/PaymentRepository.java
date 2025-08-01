package com.prashant.library.library_management.repository;

import com.prashant.library.library_management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
