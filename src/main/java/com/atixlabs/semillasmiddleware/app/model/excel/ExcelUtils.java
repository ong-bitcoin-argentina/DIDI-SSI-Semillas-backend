package com.atixlabs.semillasmiddleware.app.model.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Date;
import java.util.Objects;

public class ExcelUtils {
    //Relacionadas a la vivienda
    public static String beneficiaryAddress(Form form){
        return (form.getViviendaDireccionCalle()+" N° "+form.getViviendaDireccionNumero()+" e/ "+form.getViviendaDireccionEntreCalles());
    }

    //Relacionadas con el emprendimiento
    public static String formatAddress(Form form){
        return (form.getActividadDirCalle()+" N° "+form.getActividadDirNumero()+" e/ "+form.getActividadDirEntreCalles()+", "+
                form.getActividadDirBarrio()+", "+form.getActividadDirLocalidad()+", "+form.getActividadDirMunicipio());
    }

    public static void formatHeader(XSSFSheet sheet) {
        if (Objects.isNull(sheet))
            return;
        XSSFRow headerRow =  sheet.getRow(0);
        for(int i=0;i<headerRow.getLastCellNum() ;i++){
            XSSFCell cell = headerRow.getCell(i);
            boolean hasTwoHash = StringUtils.countMatches(cell.getStringCellValue(), "#")==2;
            if(hasTwoHash){
                String[] splittedCellContent = cell.getStringCellValue().split("#");
                String title = splittedCellContent[1];
                String subTitle = splittedCellContent[2].substring(splittedCellContent[2].lastIndexOf("/"));
                subTitle = subTitle.replaceAll("/span>", "").replaceAll("/", "_"); // clean string
                headerRow.createCell(i);
                headerRow.getCell(i).setCellValue(title + subTitle);
            }
        }
    }

}