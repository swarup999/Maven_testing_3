package com.tarento.upsmf.userManagement.services;

import com.tarento.upsmf.userManagement.model.Payment;
import com.tarento.upsmf.userManagement.model.ResponseDto;

public interface PaymentService {

    public String makePayment(Payment payment);

    /*@Autowired
    PaymentRepository paymentRepository;

    public ResponseDto makePayment(Payment payment){
        ResponseDto response = new ResponseDto("api.payment.make");
        // Calculate noOfExams based on the number of selected options
        int noOfExams = payment.getExams().size();
        // Calculate the fee amount based on noOfExams and exam fee amount (replace with actual fee calculation)
        int examFeeAmount = 100; // Example exam fee amount
        int feeAmount = calculateFee(noOfExams, examFeeAmount);
        // Set the calculated values in the FeeManage object
        payment.setNoOfExams(noOfExams);
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
    }*/
}