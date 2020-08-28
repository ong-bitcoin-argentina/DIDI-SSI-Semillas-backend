package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.Email;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.service.MailService;
import com.atixlabs.semillasmiddleware.util.EmailTemplatesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ShareStateChangeService {

    public ShareStateChangeService(MailService mailService){
        this.mailService = mailService;
    }

    private MailService mailService;

    private static final String APPROVED_TEMPLATE = "approved_identity_request_template.html";
    private static final String REJECTED_TEMPLATE = "rejected_identity_request_template.html";

    private static final String REJECT_REASON_PARAM ="{rejectReason}";
    private static final String REJECT_OBSERVATIONS_PARAM ="{rejectionObservations}";

    public void shareStateChange(IdentityValidationRequest identityValidationRequest){
        log.info("Send email to requester of the identity status.");
        if(identityValidationRequest.getRequestState().equals(RequestState.IN_PROGRESS)) return;

        Email email = Email.builder()
                .to(identityValidationRequest.getEmail())
                .subject(getSubject())
                .template(getTemplate(identityValidationRequest))
                .build();

        mailService.send(email);
    }

    private String getSubject(){
        return "Validación de Identidad a través de Semillas";
    }

    private String getTemplate(IdentityValidationRequest identityValidationRequest){
        String templateToUse = identityValidationRequest.getRequestState().equals(RequestState.SUCCESS) ? APPROVED_TEMPLATE : REJECTED_TEMPLATE;
        return EmailTemplatesUtil.replaceParams(EmailTemplatesUtil.getTemplate(templateToUse), getTemplateParameters(identityValidationRequest));
    }

    //TODO: change parameter replacement for a more light weight solution
    private Map<String, String> getTemplateParameters(IdentityValidationRequest identityValidationRequest){
        Map<String, String> parameters = new HashMap<>();

        if(identityValidationRequest.getRequestState().equals(RequestState.FAILURE)){
            parameters.put(REJECT_REASON_PARAM, identityValidationRequest.getRejectReason().getDescription());
            parameters.put(REJECT_OBSERVATIONS_PARAM, identityValidationRequest.getRejectionObservations() == null ? "-" : identityValidationRequest.getRejectionObservations());
        }

        return parameters;
    }
}
