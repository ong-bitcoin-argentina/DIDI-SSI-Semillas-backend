package com.atixlabs.semillasmiddleware.excelparser.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the final result of the processing of a file
 */
@Getter
@Setter
public class ProcessExcelFileResult {

    private int totalRows = 0;
    private int validRows = 0;
    private int totalInsertedRows = 0;
    private int totalErrorsRows = 0;
    private int totalDeletedRows = 0;
    private List<String> errorRows = new ArrayList<String>();
    private String fileName = null;
    private boolean isFileValid = true;
    private String fileError = null;

}
