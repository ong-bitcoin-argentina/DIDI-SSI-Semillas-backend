package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevokeRequestDto {
    private Boolean revokeOnlyThisCredential;
}
