package com.tarento.upsmf.userManagement.services;

import com.tarento.upsmf.userManagement.model.Payment;
import com.tarento.upsmf.userManagement.model.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    public ResponseDto makePayment(Payment payment);
}