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

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalInsertedRows() {
        return totalInsertedRows;
    }

    public void setTotalInsertedRows(int totalInsertedRows) {
        this.totalInsertedRows = totalInsertedRows;
    }

    public int getTotalErrorsRows() {
        return totalErrorsRows;
    }

    public void setTotalErrorsRows(int totalErrorsRows) {
        this.totalErrorsRows = totalErrorsRows;
    }

    public int getTotalDeletedRows() {
        return totalDeletedRows;
    }

    public void setTotalDeletedRows(int totalDeletedRows) {
        this.totalDeletedRows = totalDeletedRows;
    }

    public List<String> getErrorRows() {
        return errorRows;
    }

    public void setErrorRows(List<String> errorRows) {
        this.errorRows = errorRows;
    }

    public String getFileName() {
        return fileName;
    }

    public ProcessExcelFileResult agregarFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public boolean isFileValid() {
        return isFileValid;
    }

    public void setFileValid(boolean fileValid) {
        isFileValid = fileValid;
    }

    public String getFileError() {
        return fileError;
    }

    public void setFileError(String fileError) {
        this.fileError = fileError;
    }

    public int getValidRows() {
        return validRows;
    }
}
