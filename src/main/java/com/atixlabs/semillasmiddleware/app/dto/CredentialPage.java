package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Slf4j
@ToString
public class CredentialPage {

   Page<CredentialDto> credentialsDto;
   Integer amountOfElements;

    public CredentialPage(Page<CredentialDto> pageDto, Integer totalAmountOfItems) {
        this.credentialsDto = pageDto;
        this.amountOfElements = totalAmountOfItems;
    }

    public CredentialPage() {}
}
