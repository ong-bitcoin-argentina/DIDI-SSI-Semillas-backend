package com.atixlabs.semillasmiddleware.app.model.excel;

import com.atixlabs.semillasmiddleware.filemanager.exception.FileManagerException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExcelUtils {
    private ExcelUtils(){}

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
                subTitle = subTitle.replace("/span>", "").replace("/", "_"); // clean string
                headerRow.createCell(i);
                headerRow.getCell(i).setCellValue(title + subTitle);
            }
        }
    }

    public static void validateFormData(Form form) throws FileManagerException {
        List<ConstraintViolation<Form>> errors = Validation.buildDefaultValidatorFactory().getValidator().validate(form).stream().collect(Collectors.toList());

        if (!errors.isEmpty()){
            String campo = errors.get(0).getPropertyPath().toString();
            String msj = errors.get(0).getMessage();

            throw new FileManagerException("Se encontró un error en la información del Excel, en el campo: "
                    .concat(campo).concat(", para el formulario N° "+form.getIndex())
                    .concat(".\nError: " + msj).concat(". Favor de revisar y corregir la información."));
        }
    }

}