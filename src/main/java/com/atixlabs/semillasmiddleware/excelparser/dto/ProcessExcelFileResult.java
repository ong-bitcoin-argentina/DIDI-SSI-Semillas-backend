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

    public void addRowError(List<String> errors){
        this.getErrorRows().addAll(errors);
        this.totalErrorsRows++;
        this.addTotalRow();
    }

    public void addRowError(String error){
        this.getErrorRows().add(error);
        this.totalErrorsRows++;
        this.addTotalRow();
    }

    public void addTotalRow(){
        this.totalRows++;
    }

    public void addInsertedRows(){
        totalInsertedRows++;
    }

    public void addValidRows(){
        validRows++;
    }

    @Override
    public String toString() {
        return "ProcessExcelFileResult{" +
                "totalRows=" + totalRows +
                ", validRows=" + validRows +
                ", totalInsertedRows=" + totalInsertedRows +
                ", totalErrorsRows=" + totalErrorsRows +
                ", totalDeletedRows=" + totalDeletedRows +
                ", errorRows=" + errorRows +
                ", fileName='" + fileName + '\'' +
                ", isFileValid=" + isFileValid +
                ", fileError='" + fileError + '\'' +
                '}';
    }
}
