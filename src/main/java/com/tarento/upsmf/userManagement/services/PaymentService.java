package com.tarento.upsmf.userManagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tarento.upsmf.userManagement.model.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;


@Service
public interface PaymentService {
    public String makePayment(Payment payment) throws URISyntaxException, IOException;
}