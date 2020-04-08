package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.plaf.basic.BasicIconFactory;
import java.time.LocalDate;

@Getter
@Setter
public class BeneficiaryCategory implements Category {

    String apellido;
    String barrio;
    String correoElectronico;
    String cursos;
    String domicilio;
    String domicilio2;
    String redes;
    Long telefono;
    String direccion;
    Integer edad;
    String estadoCivil;
    String facebook;
    LocalDate fechaNacimiento;
    String localidad;
    String nacionalidad;
    String nivelInstitucion;
    String nivelEducativo;
    String nombre;
    Long numeroDocumento;
    Boolean otro;
    Boolean primaria;
    Boolean secundaria;
    Boolean talleres;
    Long telefonoCelular;
    Long telefonoFijo;
    Long telefonoReferente;
    Boolean terciario;
    String tiempoResidencia;
    String tipoDocumento;
    Integer ultimoAnoEstudio;
    Boolean universitario;


    @Override
    public void loadData(AnswerRow answerRow) {
        switch (StringUtil.cleanString(answerRow.getQuestion())){
            case "NUMERO DE DOCUMENTO":
                this.numeroDocumento = answerRow.getAnswerAsLong();
                break;
        }

    }

    @Override
    public BeneficiaryCategory getData() {
        return  this;
    }

    @Override
    public String toString() {
        return "BeneficiaryCategory{" +
                "apellido='" + apellido + '\'' +
                ", barrio='" + barrio + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", cursos='" + cursos + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", domicilio2='" + domicilio2 + '\'' +
                ", redes='" + redes + '\'' +
                ", telefono=" + telefono +
                ", direccion='" + direccion + '\'' +
                ", edad=" + edad +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", facebook='" + facebook + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", localidad='" + localidad + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", nivelInstitucion='" + nivelInstitucion + '\'' +
                ", nivelEducativo='" + nivelEducativo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", numeroDocumento=" + numeroDocumento +
                ", otro=" + otro +
                ", primaria=" + primaria +
                ", secundaria=" + secundaria +
                ", talleres=" + talleres +
                ", telefonoCelular=" + telefonoCelular +
                ", telefonoFijo=" + telefonoFijo +
                ", telefonoReferente=" + telefonoReferente +
                ", terciario=" + terciario +
                ", tiempoResidencia='" + tiempoResidencia + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", ultimoAñoEstudio=" + ultimoAnoEstudio +
                ", universitario=" + universitario +
                '}';
    }
}
