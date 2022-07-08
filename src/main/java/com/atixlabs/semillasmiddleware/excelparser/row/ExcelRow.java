package com.atixlabs.semillasmiddleware.excelparser.row;

import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;


/**
 * parent class of the parsers of each type of row
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public abstract class ExcelRow {

    protected int rowNum;
    protected boolean exists = false;
    protected int cellIndex = 0;
    protected String cellIndexName = "None";
    protected String cellIndexDescription = "";


    protected ExcelRow(Row row) throws InvalidRowException {
        try {
            this.rowNum = row.getRowNum()+1;//must +1 due to headers
            this.parseRow(row);
        } catch (Exception e) {
            log.error("ExcelRow: ", e);
            throw new InvalidRowException(e.getMessage());
        }
    }

    protected String getCellWithType(Row row, int cellIndex, String description){
        this.saveCellData(cellIndex, description);
        Row validRow = this.validateCellToRead(row, cellIndex);
        if(validRow != null){
            if (validRow.getCell(cellIndex).getCellType().equals(CellType.STRING)){
                return this.getCellStringValue(validRow, cellIndex, description);
            }
            if (validRow.getCell(cellIndex).getCellType().equals(CellType.NUMERIC)) {
                return this.getCellLongValue(validRow, cellIndex, description).toString();
            }
        }
        return null;
    }

    protected String getCellStringValue(Row row, int cellIndex, String description) {
        this.saveCellData(cellIndex, description);
        Row validRow = this.validateCellToRead(row, cellIndex);
        if(validRow == null)
            return null;
        row.getCell(cellIndex).setCellType(CellType.STRING);// NOSONAR
        return row.getCell(cellIndex).getStringCellValue();
    }

    protected Long getCellLongValue(Row row, int cellIndex, String description) {
        this.saveCellData(cellIndex, description);
        Row validRow = this.validateCellToRead(row, cellIndex);
        if(validRow == null)
            return null;
        return (long) validRow.getCell(cellIndex).getNumericCellValue();
    }

    protected Row validateCellToRead(Row row, int cellIndex){
        if (row == null || row.getCell(cellIndex) == null)
            return null;
        this.cellIndexName = row.getCell(cellIndex).getAddress().formatAsString();
        if (row.getCell(cellIndex).getCellType() == CellType.BLANK)
            return null;
        return row;
    }

    private void saveCellData(int cellindex, String cellIndexDescription) {
        this.cellIndex = cellindex;
        this.cellIndexDescription = cellIndexDescription;
    }

    protected abstract void parseRow(Row row);


    public String toString(Row row){
        Iterator<Cell> cellIterator = row.cellIterator();
        StringBuilder cellString = new StringBuilder();

        while (cellIterator.hasNext()){
            cellString.append( " | " + cellIterator.next().toString());
        }

        return cellString.toString();
    }


}

