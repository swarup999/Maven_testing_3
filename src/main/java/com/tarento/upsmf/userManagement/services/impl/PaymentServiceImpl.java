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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@PropertySource({ "classpath:application.properties" })
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private Environment env;

    private static Environment environment;

    @Autowired
    PaymentRepository paymentRepository;

    private String REGISTRATION_PAYMENT_GATEWAY_ENDPOINT;
    private String AFFILIATION_PAYMENT_GATEWAY_ENDPOINT;

    private String AES_KEY_FOR_PAYMENT_SUCCESS;

    @PostConstruct
    public void init(){
        environment = env;
        REGISTRATION_PAYMENT_GATEWAY_ENDPOINT = getPropertyValue("registration_payment_Gateway_EndPoint");
        AFFILIATION_PAYMENT_GATEWAY_ENDPOINT = getPropertyValue("affiliation_payment_Gateway_EndPoint");
        AES_KEY_FOR_PAYMENT_SUCCESS = getPropertyValue("aes_key_for_payment_success");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    @Override
    public ResponseEntity<String> makePayment(Map<String, String> requestData) {
        logger.info("payment details...{} ", requestData);
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        String mandatoryFields = requestData.get("mandatory fields");
        logger.info("mandatory fields details...{} ", mandatoryFields);
        if(mandatoryFields != null && !mandatoryFields.isEmpty()) {
            String[] split = mandatoryFields.split(Pattern.quote("|"));
            List<String> strings = Arrays.asList(split);
            String strEndPoint = "";
            if (strings.contains("registration")) {
                strEndPoint = REGISTRATION_PAYMENT_GATEWAY_ENDPOINT;
            } else if (strings.contains("registration")) {
                strEndPoint = AFFILIATION_PAYMENT_GATEWAY_ENDPOINT;
            }
            String responseString = "";
            if ((requestData != null) && (requestData.get("Response Code") != null)
                    && requestData.get("Response Code").equals("E000")) {
                responseString = strEndPoint + "?resp=success";
                logger.info("Payment is successful.");
            } else {
                responseString = strEndPoint + "?resp=failure";
                logger.info("Payment failed.");
            }
            logger.info("responseString details...{} ", responseString);
            httpHeaders.setLocation(URI.create(responseString));
            return new ResponseEntity<String>(null, httpHeaders, HttpStatus.FOUND);
        }
        return new ResponseEntity<String>(null, httpHeaders, HttpStatus.NOT_FOUND);
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