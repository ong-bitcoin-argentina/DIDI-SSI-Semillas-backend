package com.atixlabs.semillasmiddleware.app.model.excel;

import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
                String subTitle = splittedCellContent[2].substring(splittedCellContent[2].indexOf("/"));
                subTitle = subTitle.replaceAll("/span>", "").replaceAll("/", "_"); // clean string
                headerRow.createCell(i);
                headerRow.getCell(i).setCellValue(title + subTitle);
            }
        }
    }

    public static void validateFormData(Form form) throws FileManagerException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<Form>> errors = validator.validate(form);

        if (!errors.isEmpty()){
            throw new FileManagerException("Se encontró un error en la información del Excel, en el campo: "
                    .concat(errors.stream().findFirst().get().getPropertyPath().toString()).concat(", para el formulario N° "+form.getIndex())
                    .concat(".\nError: " + errors.stream().findFirst().get().getMessage()).concat(". Favor de revisar y corregir la información."));
        }
    }

}