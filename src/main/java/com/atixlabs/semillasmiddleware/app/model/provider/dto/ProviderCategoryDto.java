package com.atixlabs.semillasmiddleware.app.model.provider.dto;


import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class ProviderCategoryDto implements Serializable {

    private long id;
    private String name;
}
