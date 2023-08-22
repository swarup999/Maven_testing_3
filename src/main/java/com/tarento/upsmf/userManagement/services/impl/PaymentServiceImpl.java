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

    @PostConstruct
    public void init(){
        environment = env;
        PAYMENT_GATEWAY_ENDPOINT = getPropertyValue("paymentGatewayEndPoint");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    @Override
    public ResponseEntity<String> makePayment(Payment payment) throws URISyntaxException, IOException {
        logger.info("payment details...{} ", payment);
        /**
         * save these attributes to DB
         */
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.setLocation(URI.create(PAYMENT_GATEWAY_ENDPOINT+ "?resp=success"));
        return new ResponseEntity<String>(null,httpHeaders, HttpStatus.FOUND);
    }
}