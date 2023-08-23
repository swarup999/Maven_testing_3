package com.tarento.upsmf.userManagement.services.impl;

import com.tarento.upsmf.userManagement.model.Payment;
import com.tarento.upsmf.userManagement.repository.PaymentRepository;
import com.tarento.upsmf.userManagement.services.PaymentService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@PropertySource({ "classpath:application.properties" })
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private Environment env;

    private static Environment environment;

    @Autowired
    PaymentRepository paymentRepository;

    private String PAYMENT_GATEWAY_ENDPOINT;

    private String AES_KEY_FOR_PAYMENT_SUCCESS;

    @PostConstruct
    public void init(){
        environment = env;
        PAYMENT_GATEWAY_ENDPOINT = getPropertyValue("paymentGatewayEndPoint");
        AES_KEY_FOR_PAYMENT_SUCCESS = getPropertyValue("aes_key_for_payment_success");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    @Override
    public ResponseEntity<String> makePayment(Map<String, String> requestData) {
        logger.info("payment details...{} ", requestData);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        String responseString = "";
        if( (requestData != null ) && (requestData.get("Response_Code")!= null)
                && requestData.get("Response_Code").equals("E000")) {
            responseString = PAYMENT_GATEWAY_ENDPOINT+ "?resp=success";
            logger.info("Payment is successful.");
        } else {
            responseString = PAYMENT_GATEWAY_ENDPOINT+ "?resp=failure";
            logger.info("Payment failed.");
        }
        httpHeaders.setLocation(URI.create(responseString));
        return new ResponseEntity<String>(null,httpHeaders, HttpStatus.FOUND);
        /*if( (requestData != null) &&
                (requestData.get("Total_Amount") != null ) &&
                (requestData.containsKey("Total_Amount") &&
                        (requestData.get("Response_Code")!= null) &&
                        requestData.get("Response_Code").equals("E000"))) {


            // Process payment success logic
            String verificationKey = requestData.get("ID") + "|" +
                    requestData.get("Response_Code") + "|" +
                    requestData.get("Unique_Ref_Number") + "|" +
                    requestData.get("Service_Tax_Amount") + "|" +
                    requestData.get("Processing_Fee_Amount") + "|" +
                    requestData.get("Total_Amount") + "|" +
                    requestData.get("Transaction_Amount") + "|" +
                    requestData.get("Transaction_Date") + "|" +
                    requestData.get("Interchange_Value") + "|" +
                    requestData.get("TDR") + "|" +
                    requestData.get("Payment_Mode") + "|" +
                    requestData.get("SubMerchantId") + "|" +
                    requestData.get("ReferenceNo") + "|" +
                    requestData.get("TPS") + "|" +
                    AES_KEY_FOR_PAYMENT_SUCCESS;
//            String encryptedMessage = sha512Hash(verificationKey);

             // save these attributes to DB
            httpHeaders.setLocation(URI.create(PAYMENT_GATEWAY_ENDPOINT+ "?resp=success"));
            } else {
            httpHeaders.setLocation(URI.create(PAYMENT_GATEWAY_ENDPOINT+ "?resp=failure"));*/
    }

    private String sha512Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error occurred while encrypting",e);
            return null;
        }
    }
}