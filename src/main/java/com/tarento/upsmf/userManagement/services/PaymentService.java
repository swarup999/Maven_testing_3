package com.tarento.upsmf.userManagement.services;

import com.tarento.upsmf.userManagement.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


@Service
public interface PaymentService {
    public ResponseEntity<String> makePayment(Map<String, String> requestData) throws URISyntaxException, IOException;
}