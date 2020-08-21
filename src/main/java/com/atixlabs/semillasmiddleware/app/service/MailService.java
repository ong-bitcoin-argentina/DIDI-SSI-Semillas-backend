package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.exceptions.EmailNotSentException;
import com.atixlabs.semillasmiddleware.app.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MailService {

    @Value("${mailgun.api_key}")
    private String API_KEY;

    @Value("${mailgun.domain}")
    private String DOMAIN;

    @Value("${mailgun.issuer}")
    private String ISSUER;

    @Value("${mailgun.cc}")
    private String CC;

    private static final String API_URL = "https://api.mailgun.net/v3/";
    private static final String ENDPOINT = "/messages";
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "to";
    private static final String SUBJECT_PARAM = "subject";
    private static final String TEMPLATE_PARAM = "html";
    private static final String CC_PARAM = "cc";


    private final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    //Entry point to send an email, receives an email fully filled.
    public void send(Email email) {
        log.info("Sending email to["+email.getTo()+"] subject["+email.getSubject()+"] template["+email.getTemplate()+"]");
        RequestBody body = getBody(email);
        Request request = getRequest(body);
        log.info("Request obtained type["+request.method()+"] url["+request.url()+"]" );
        Call call = okHttpClient.newCall(request);
        try{
            Response response = call.execute();
            log.info("Succesfully sent email response status["+response.code()+"] msg["+response.message()+"]");
        }catch (IOException ioe){
            log.error(ioe.getMessage());
            throw new EmailNotSentException(ioe.getMessage());
        }

    }

    private FormBody getBody(Email email){
        return new FormBody.Builder()
                .add(FROM_PARAM, ISSUER)
                .add(TO_PARAM, email.getTo())
                .add(SUBJECT_PARAM,email.getSubject())
                .add(TEMPLATE_PARAM, email.getTemplate())
                .add(CC_PARAM, CC)
                .build();
    }

    //Returns request from url
    private Request getRequest(RequestBody body){
        return new Request.Builder()
                .url(API_URL + DOMAIN + ENDPOINT)
                .addHeader("Authorization", Credentials.basic("api", API_KEY))
                .post(body)
                .build();
    }


}
