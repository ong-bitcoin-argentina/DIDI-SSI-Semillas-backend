package com.atixlabs.semillasmiddleware.excelparser.app.dto;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;

@Getter
@Setter
public class SurveyInfo extends ExcelRow {
    public String area;
    public String region;
    public String subregion;
    public String zone;
    public String supervisor;
    public String territory;
    public String promoter;
    public String surveyName;
    public int month;
    public Date date;
    public int posId;
    public String businessName;
    public String fantasyName;
    public String address;

    public SurveyInfo(Row row) throws InvalidRowException {
        super(row);
    }

    public String getSurveyName(){
        return surveyName;
    }

    @Override
    protected void parseRow(Row row) {
        this.setArea(getCellStringValue(row, 0,"AREA"));
        this.setRegion(getCellStringValue(row, 1,"REGION"));
        this.setSubregion(getCellStringValue(row, 2,"SUBREGION"));
        this.setZone(getCellStringValue(row, 3,"ZONA"));
        this.setSupervisor(getCellStringValue(row, 4,"SUPERVISOR"));
        this.setTerritory(getCellStringValue(row, 5,"TERRITORIO"));
        this.setPromoter(getCellStringValue(row, 6,"PROMOTOR"));
        this.setSurveyName(getCellStringValue(row, 7,"FORMULARIO"));
        this.setMonth(getCellIntValue(row, 8,"MES"));
        this.setDate(getCellStringValue(row, 9,"FECHA DE RELEVAMIENTO"));
        this.setPosId(getCellIntValue(row, 10,"PDV"));
        this.setBusinessName(getCellStringValue(row, 11,"RAZON SOCIAL"));
        this.setFantasyName(getCellStringValue(row, 12,"NOMBRE DE FANTASIA"));
        this.setAddress(getCellStringValue(row, 13,"DIRECCION"));
    }

    public boolean isValid() {
        return true;
        //TODO create and pass validator
    }

}
