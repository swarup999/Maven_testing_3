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
    public ResponseDto makePayment(Payment payment) {

        ResponseDto response = new ResponseDto("api.payment.make");
        // Calculate noOfExams based on the number of selected options
        //int noOfExams = payment.getExams().size();
        // Calculate the fee amount based on noOfExams and exam fee amount (replace with actual fee calculation)
        int examFeeAmount = 100; // Example exam fee amount
        int feeAmount = calculateFee(10, examFeeAmount);
        // Set the calculated values in the FeeManage object
        payment.setNoOfExams(10);
        payment.setFeeAmount(feeAmount);
        // Save the FeeManage object
        try {
            Payment result = paymentRepository.save(payment);
            response.put("message", "Success");
            response.put("response", "Created.");
            response.setResponseCode(HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error saving fee details");
            response.put("response", "Failed to create a message");
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
    public Integer calculateFee(Integer noOfExams, Integer examFeeAmount) {
        return noOfExams * examFeeAmount;
    }
}