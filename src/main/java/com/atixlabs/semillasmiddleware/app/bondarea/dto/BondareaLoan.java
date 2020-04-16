package com.atixlabs.semillasmiddleware.app.bondarea.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BondareaLoan {

    private String pp; // ID de producto de préstamo (Ej. B26F5FKZ)
    private String ppt; // Nombre del producto de préstamo (Ej. Recurrentes)
    private String sta; // Estado del préstamo (Ej. Preparación, Activo, Finalizado)
    private int staint; // Estado numérico del préstamo (Ej.0=Preparación, 55= Activo, 60=finalizado)
    private String t; // Deccripcion de estado ?
    private String id; // ID del crédito individual. Para créditosgrupales representa el tramo del crédito grupal   (Ej. B26F5FKZ)
    private String id_pg; // ID del crédito grupal y su estado (Ej.55-B26F5FKZ)
    private String pg; // Nombre asignado al crédito (Ej. Ciclo 2)
    private String fOt; // Fecha de otorgamiento cuentas
    private String cuentasTag; // Nombre del solicitante del tramo (Ej. Perez, Juan)
    private String usr; // ID del solicitante del tramo (Ej. B26F5FKZ)
    private Long dni; // Nro. de documento del solicitante del tramo (Ej. 99999999)
    private Float m; // Monto del crédito del tramo (Ej. 10000)
    private String fPri; // Fecha de primera cuota
    private Float sv; // Saldo vencido del crédito individual, compuesto por capital, intereses, seguros y cargos (Ej. 1845.24)
}
