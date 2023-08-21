package com.tarento.upsmf.userManagement.repository;

import com.tarento.upsmf.userManagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    @Query(value = "SELECT * FROM fee_details f LEFT JOIN exam_details e ON f.fee_id = e.fee_id WHERE f.fee_id = :id", nativeQuery = true)
    Optional<Payment> findByIdWithExamsNative(@Param("id") Integer id);
}