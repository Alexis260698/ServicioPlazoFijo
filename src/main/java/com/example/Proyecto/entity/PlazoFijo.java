package com.example.Proyecto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PlazoFijo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlazoFijo {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float monto;
    private Integer plazoDias;



    @OneToOne(cascade=CascadeType.ALL)
    private Cuenta cuenta;


    private String tasa;
    private String fechaVencimiento;
    private Float capital;
    private Float intereses;

    @Override
    public String toString() {
        return "PlazoFijo{" +
                "id=" + id +
                ", monto=" + monto +
                ", plazoDias=" + plazoDias +
                ", cuenta=" + cuenta +
                ", tasa='" + tasa + '\'' +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", capital=" + capital +
                ", intereses=" + intereses +
                '}';
    }
}