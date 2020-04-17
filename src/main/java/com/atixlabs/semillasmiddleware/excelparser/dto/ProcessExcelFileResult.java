package com.atixlabs.semillasmiddleware.excelparser.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the final result of the processing of a file
 */
@NoArgsConstructor
@Getter
@Setter
@Component
public class ProcessExcelFileResult {

    private int totalRows = 0;
    private int validRows = 0;
    private int totalInsertedRows = 0;
    private int totalErrorsRows = 0;
    private int totalDeletedRows = 0;
    private int totalProcessedForms = 0;
    //private List<String> errorRows = new ArrayList<String>();
    private List<ExcelErrorDetail> errorRows = new ArrayList<>();

    private String fileName = null;
    private boolean isFileValid = true;
    private String fileError = null;
/*
    public ProcessExcelFileResult(){
        totalRows = 0;
        validRows = 0;
        totalInsertedRows = 0;
        totalErrorsRows = 0;
        totalDeletedRows = 0;
        totalProcessedForms = 0;
        List<ExcelErrorDetail> errorRows = new ArrayList<>();

        String fileName = null;
        boolean isFileValid = true;
        String fileError = null;
    }
*/

    /*public void addRowError(String errorBody){
        ExcelErrorDetail  excelErrorDetail = new ExcelErrorDetail(errorBody);
        this.getErrorRows().add(excelErrorDetail);
        this.totalErrorsRows++;
        this.addTotalRow();
    }*/

    public void addRowError(Integer errorHeader, String errorBody){
        this.errorRows.add(new ExcelErrorDetail(String.valueOf(errorHeader), errorBody));
        this.totalErrorsRows++;
        this.addTotalRow();
    }

    public void addRowError(String errorHeader, String errorBody){
        this.errorRows.add(new ExcelErrorDetail(errorHeader, errorBody));
        this.totalErrorsRows++;
        this.addTotalRow();
    }

    public void addDeletedRow(){this.totalDeletedRows++;}
    public void addTotalRow(){
        this.totalRows++;
    }
    public void addInsertedRows(){
        this.totalInsertedRows++;
    }
    public void addProcessedForms(){ this.totalProcessedForms++;}
    public void addValidRows(){
        this.validRows++;
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
