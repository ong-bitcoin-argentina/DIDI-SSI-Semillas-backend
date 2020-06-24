package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@ToString
public class CredentialPage {

   Page<CredentialDto> credentialsDto;
   Long amountOfElements;

    public CredentialPage(Page<CredentialDto> pageDto, Long totalAmountOfItems) {
        this.credentialsDto = pageDto;
        this.amountOfElements = totalAmountOfItems;
    }

    public CredentialPage() {}
}
