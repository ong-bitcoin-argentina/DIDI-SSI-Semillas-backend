package com.atixlabs.semillasmiddleware.excelparser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelErrorDetail {

    private String errorHeader;
    private String errorBody;
    private ExcelErrorType excelErrorType;
    private String data;

    public ExcelErrorDetail(String errorHeader, String errorBody){
        this(errorHeader, errorBody, ExcelErrorType.OTHER, "");
    }
    public ExcelErrorDetail(String errorHeader, String errorBody, ExcelErrorType excelErrorType, String data){
        this.errorHeader = errorHeader;
        this.errorBody = errorBody;
        this.excelErrorType = excelErrorType;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "errorHeader='" + errorHeader + '\'' +
                ", errorBody='" + errorBody + '\'' +
                '}';
    }
}
