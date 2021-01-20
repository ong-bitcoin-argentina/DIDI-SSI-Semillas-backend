package com.atixlabs.semillasmiddleware.excelparser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the final result of the processing of a file
 */
@NoArgsConstructor
@Getter
@Setter
public class ProcessExcelFileResult {

    private int totalReadRows = 0;
    private int totalValidRows = 0;
    private int totalErrorsRows = 0;
    private int totalEmptyRows = 0;
    private int totalWarnRows = 0;
    private int totalProcessedForms = 0;
    private String fileName = null;
    private String downloadableFileName;

    private List<ExcelWarnDetail> warnRows = new ArrayList<>();
    private List<ExcelErrorDetail> errorRows = new ArrayList<>();
    @JsonIgnore
    private List<ExcelErrorDetail> debugRows = new ArrayList<>();


    public void addRowWarn(String warnHeader, String warnBody){
        this.warnRows.add(new ExcelWarnDetail(warnHeader, warnBody));
        this.totalWarnRows++;
    }

    public void addRowError(ExcelErrorDetail excelErrorDetail){
        this.errorRows.add(excelErrorDetail);
        this.totalErrorsRows++;
    }

    public void addTotalReadRow(){
        this.totalReadRows++;
    }
    public void addTotalValidRows(){
        this.totalValidRows++;
    }
    public void addEmptyRow(){this.totalEmptyRows++;}
    public void addTotalProcessedForms(){ this.totalProcessedForms++;}

    public void addRowDebug(ExcelErrorDetail excelErrorDetail){
        this.debugRows.add(excelErrorDetail);
    }

    @Override
    public String toString() {
        return "ProcessExcelFileResult{" +
                "totalReadRows=" + totalReadRows +
                ", totalValidRows=" + totalValidRows +
                ", totalErrorsRows=" + totalErrorsRows +
                ", totalEmptyRows=" + totalEmptyRows +
                ", totalProcessedForms=" + totalProcessedForms +
                ", fileName='" + fileName + '\'' +
                ", errorRows=" + errorRows +
                '}';
    }
}
