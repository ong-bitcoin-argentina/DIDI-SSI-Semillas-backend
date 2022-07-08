package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.row.ExcelRow;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Slf4j
public class SancorPolicyRow extends ExcelRow {

    private String branchDescription;

    private Long productId;

    private Long policy;

    private String policyClient;

    private String policyClientName;
    private Long certificateNumber;
    private String certificateClient;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate validityFrom;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private LocalDate validityTo;

    private String certificateClientName;

    private String certificateClientAddress;

    public SancorPolicyRow(Row row) throws InvalidRowException {
        super(row);
    }

    public boolean isEmpty() {
        return this.certificateClient == null;
    }

    @Override
    protected void parseRow(Row row) {
        this.branchDescription = this.getCellStringValue(row, 0, "Descripción Ramo");
        this.productId = this.getCellLongValue(row,1,"Producto");
        this.policy = this.getCellLongValue(row,2,"Póliza");
        this.policyClient = this.getCellStringValue(row, 3, "Cliente Poliza");
        this.policyClientName = this.getCellStringValue(row, 4, "Nombre Cliente Póliza");
        this.certificateNumber = this.getCellLongValue(row,5,"Certificado");
        this.certificateClient = this.getCellStringValue(row, 6, "Nombre Cliente Póliza");
        this.validityFrom = this.getCellStringToDate(row, 7, "Ini. Vigencia Cert. Ori.");
        this.validityTo = this.getCellStringToDate(row, 8, "Fin Vigencia Cert.");
        this.certificateClientName = this.getCellStringValue(row, 9, "Nombre Cliente Cert.");
        this.certificateClientAddress = this.getCellStringValue(row, 10, "Domicilio");

    }

    public LocalDate getCellStringToDate(Row row, int cellIndex, String description) {
        String dateString = null;
        String datePattern = "dd/MM/yyyy";

        try {
            Date date = row.getCell(cellIndex).getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        }
        catch (Exception e){

             log.error("Invalid date on row {} cell {}", row.getRowNum(), cellIndex,e);
        }

        try {

            log.info("try date as string");
            dateString = getCellStringValue(row, cellIndex, description).trim();
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(datePattern));

        }
        catch (Exception e){

            log.error("Invalid date on row {} cell {}", row.getRowNum(), cellIndex,e);
        }
        return null;
    }

}
