package com.atixlabs.semillasmiddleware.excelparser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelWarnDetail {


    private String warnHeader;
    private String warnBody;

    public ExcelWarnDetail(String warnHeader, String warnBody){
        this.warnHeader = warnHeader;
        this.warnBody = warnBody;
    }
}
