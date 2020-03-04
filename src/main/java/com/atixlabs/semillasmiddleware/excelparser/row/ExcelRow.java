package com.atixlabs.semillasmiddleware.excelparser.row;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;


/**
 * parent class of the parsers of each type of row
 */
@Getter
@Setter
@Slf4j
public abstract class ExcelRow {
    protected int rowNum;
    protected String errorMessage;
    protected boolean isValid = true;
    protected boolean exists = false;
    protected int cellIndex = 0;
    protected String cellIndexName = "None";
    protected String cellIndexDescription = "";

    ExcelRow(Row row) throws InvalidRowException {
        try {
            this.rowNum = row.getRowNum();
            this.parseRow(row);
        } catch (Exception e) {
            log.error(getStringError(), e);
            throw new InvalidRowException(getStringError() + e.getMessage());
        }
    }

    String getCellStringValue(Row row, int cellindex, String descripcion) {
        this.saveCellData(cellindex, descripcion);
        if (row == null || row.getCell(cellindex) == null)
            return null;
        this.cellIndexName = row.getCell(cellindex).getAddress().formatAsString();
        if (row.getCell(cellindex).getCellType() == CellType.BLANK)
            return null;
        row.getCell(cellindex).setCellType(CellType.STRING);
        return row.getCell(cellindex).getStringCellValue();
    }

    Long getCellLongValue(Row row, int cellindex, String descripcion) {
        this.saveCellData(cellindex, descripcion);
        if (row == null || row.getCell(cellindex) == null)
            return null;
        this.cellIndexName = row.getCell(cellindex).getAddress().formatAsString();
        if (row.getCell(cellindex).getCellType() == CellType.BLANK)
            return null;
        return (long) row.getCell(cellindex).getNumericCellValue();
    }

    Double getCellDoubleValue(Row row, int cellindex, String descripcion) {
        this.saveCellData(cellindex, descripcion);
        if (row == null || row.getCell(cellindex) == null)
            return null;
        this.cellIndexName = row.getCell(cellindex).getAddress().formatAsString();
        if (row.getCell(cellindex).getCellType() == CellType.BLANK)
            return null;
        return row.getCell(cellindex).getNumericCellValue();
    }

    Integer getCellIntValue(Row row, int cellindex, String descripcion) {
        this.saveCellData(cellindex, descripcion);
        if (row == null || row.getCell(cellindex) == null)
            return null;
        this.cellIndexName = row.getCell(cellindex).getAddress().formatAsString();
        if (row.getCell(cellindex).getCellType() == CellType.BLANK)
            return null;
        return (int) row.getCell(cellindex).getNumericCellValue();
    }

    Boolean getCellBooleanValue(Row row, int cellindex, String descripcion) {
        this.saveCellData(cellindex, descripcion);
        if (row == null || row.getCell(cellindex) == null)
            return null;
        this.cellIndexName = row.getCell(cellindex).getAddress().formatAsString();
        if (row.getCell(cellindex).getCellType() == CellType.BLANK)
            return null;
        return row.getCell(cellindex).getBooleanCellValue();
    }

    Long getCellStringToLongValue(Row row, int cellindex, String descripcion) {
        return Long.parseLong(getCellStringValue(row, cellindex, descripcion));
    }

    private void saveCellData(int cellindex, String cellIndexDescription) {
        this.cellIndex = cellindex;
        this.cellIndexDescription = cellIndexDescription;
    }

    protected String getStringError() {
        return "Cell " + cellIndexName + ", Error: " + cellIndexDescription + " ";
    }


    protected abstract void parseRow(Row row);
}

