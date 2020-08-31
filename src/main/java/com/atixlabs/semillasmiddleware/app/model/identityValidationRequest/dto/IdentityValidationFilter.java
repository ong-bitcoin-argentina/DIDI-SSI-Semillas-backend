package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RejectReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class IdentityValidationFilter {
    private Optional<LocalDate> dateFrom;
    private Optional<LocalDate> dateTo;
    private Optional<String> criteriaQuery;
    private Optional<RejectReason> rejectReason;
}
