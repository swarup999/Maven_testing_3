package com.tarento.upsmf.userManagement.services;

import com.tarento.upsmf.userManagement.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;


@Service
public interface PaymentService {
    public ResponseEntity<String> makePayment(Payment payment) throws URISyntaxException, IOException;
}