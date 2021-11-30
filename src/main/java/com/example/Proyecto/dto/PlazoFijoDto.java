package com.example.Proyecto.dto;

import com.example.Proyecto.entity.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlazoFijoDto {

    private String fechaVencimiento;
    private Float capital;
    private Float intereses;

}
