package com.example.Proyecto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Cuota")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cuota {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private Float valorCuota;
    private String proximaCuota;
    private Float importeMaximoCuota;

}
