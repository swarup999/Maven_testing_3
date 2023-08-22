package com.tarento.upsmf.userManagement.services.impl;

import com.tarento.upsmf.userManagement.model.Payment;
import com.tarento.upsmf.userManagement.model.ResponseDto;
import com.tarento.upsmf.userManagement.repository.PaymentRepository;
import com.tarento.upsmf.userManagement.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
//    @Override
    public String makePayment(Payment payment) {
        //read params
         System.out.println(payment);
        //check status

        //if valid

        //save transaction details with success
        //redirect to success page with success params - transaction id, amount,
        //return "https://applicant.upsmfac.org?resp=success&transactionId{}=&amount={}";
        //if invalid
//        save transactions details with error
        //redirect to error page with error params - transaction id, error code , amount
        return "https://applicant.upsmfac.org?resp=fail&transactionId{}=&amount={}";

    }
}