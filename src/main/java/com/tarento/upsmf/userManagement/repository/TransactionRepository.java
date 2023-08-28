package com.tarento.upsmf.userManagement.repository;

import com.tarento.upsmf.userManagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
