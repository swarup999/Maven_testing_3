package com.tarento.upsmf.userManagement.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarento.upsmf.userManagement.model.Payment;
import com.tarento.upsmf.userManagement.repository.PaymentRepository;
import com.tarento.upsmf.userManagement.services.PaymentService;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init(){
        environment = env;
        PAYMENT_GATEWAY_ENDPOINT = getPropertyValue("paymentGatewayEndPoint");
    }

    public static String getPropertyValue(String property){
        return environment.getProperty(property);
    }

    @Override
    public String makePayment(Payment payment) throws URISyntaxException, IOException {
        logger.info("payment details...{} ", payment);
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PAYMENT_GATEWAY_ENDPOINT);
        String strPaymentURL = mapper.writeValueAsString(payment);
        StringEntity entity = new StringEntity(strPaymentURL);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        logger.info("Response from httpClient call : {}", response);
        String responseBody = EntityUtils.toString(response.getEntity());
        logger.info("Response from keycloak Rest API call : {}", responseBody);
        return responseBody;
    }
}