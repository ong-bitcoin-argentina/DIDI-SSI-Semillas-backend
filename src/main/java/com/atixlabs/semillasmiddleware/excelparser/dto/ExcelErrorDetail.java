package com.atixlabs.semillasmiddleware.excelparser.dto;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class ExcelErrorDetail {

    private String errorHeader;
    private String errorBody;
    private ExcelErrorType errorType;
    private Long documentNumber;
    private String name;
    private String lastName;
    private Long credentialId;
    private String category;
}
