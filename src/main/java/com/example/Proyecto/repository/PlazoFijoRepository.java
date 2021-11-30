package com.example.Proyecto.repository;

import com.example.Proyecto.dao.PlazoFijoDao;
import com.example.Proyecto.dto.PlazoFijoDto;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.entity.PlazoFijo;
import jdk.swing.interop.SwingInterOpUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlazoFijoRepository {

    @Autowired
    PlazoFijoDao plazoFijoDao;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModelMapper modelMapper;

    @Value("${plazoF.monto1}")
    private Float monto1;

    @Value("${plazoF.monto2}")
    private Float monto2;

    @Value("${plazoF.monto3}")
    private Float monto3;

    @Value("${plazoF.porcentaje1}")
    private double porcentaje1;

    @Value("${plazoF.porcentaje2}")
    private double porcentaje2;

    @Value("${plazoF.porcentaje3}")
    private double porcentaje3;

    public Cliente crearPlazoFijo(Cliente cliente, PlazoFijo plazoFijo, Cuenta cuenta) {

        String fechaVencimiento = calcularfecha(plazoFijo.getPlazoDias());
        plazoFijo.setFechaVencimiento(fechaVencimiento);
        plazoFijo.setTasa(getTaza(plazoFijo.getMonto()) + "%");
        plazoFijo.setIntereses(Float.parseFloat(getInteres(plazoFijo.getMonto())));
        plazoFijo.setCapital(plazoFijo.getMonto() + plazoFijo.getIntereses());

        Cliente clienten = hacerRetiroCuenta(cliente, cuenta.getCbu(), plazoFijo);
        clienten.getPlazoFijoLista().add(plazoFijo);
        return clienten;
    }


    public Cliente enviarDatosCliente(Cliente cliente) {
        restTemplate.put("http://localhost:8080/actualizarCliente", cliente);
        return cliente;
    }

    public Cliente hacerRetiroCuenta(Cliente cliente, String cbu, PlazoFijo plazoFijo) {

        for (Cuenta c : cliente.getCuentas()) {
            if (c.getCbu().equalsIgnoreCase(cbu)) {
                String saldo = (c.getSaldo() - plazoFijo.getMonto()) + "";
                c.setSaldo(Float.parseFloat(saldo));
                plazoFijo.setCuenta(c);
            }
        }
        return cliente;
    }

    public String calcularfecha(int dias) {
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedLocalDate = fecha.format(formatter);
        return sumarDias(formattedLocalDate + " 15:00:00", dias);
    }

    public static String sumarDias(String fecha, int days) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss");
        LocalDateTime fecha2 = LocalDateTime.parse(fecha, formateador);
        fecha2 = fecha2.plusDays(days);
        return fecha2.format(formateador).substring(0, 10);
    }

    public Cliente consumirCliente(String usuario) {
        try {
            Cliente cliente = restTemplate.getForObject("http://localhost:8080/buscarCliente/" + usuario, Cliente.class);
            return cliente;
        } catch (Exception e) {
            return new Cliente();
        }
    }

    public String getTaza(double cantidad) {
        if (cantidad > monto3) {
            return porcentaje3 + "";
        } else if (cantidad > monto2) {
            return porcentaje2 + "";
        } else if (cantidad > monto1) {
            return porcentaje1 + "";
        }
        return "0";
    }


    public List<String> getTasas() {
        List<String> tazasDelDia = new ArrayList<>();
        tazasDelDia.add("si el monto es mayor a: " + monto1 + " el interes sera del: " + porcentaje1 + "%");
        tazasDelDia.add("si el monto es mayor a: " + monto2 + " el interes sera del: " + porcentaje2 + "%");
        tazasDelDia.add("si el monto es mayor a: " + monto3 + " el interes sera del: " + porcentaje3 + "%");
        return tazasDelDia;
    }


    public String getInteres(double cantidad) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (cantidad > monto3) {
            double valor = cantidad / 100;
            return df.format(valor * porcentaje3) + "";
        } else if (cantidad > monto2) {
            double valor = cantidad / 100;
            return df.format(valor * porcentaje2) + "";
        } else if (cantidad > monto1) {
            double valor = cantidad / 100;
            return df.format(valor * porcentaje1) + "";
        }
        return "0";
    }


    public Cuenta tieneCuenta(Cliente cliente, String cuenta) {
        for (Cuenta c : cliente.getCuentas()) {
            if (c.getTipoDeCuenta().equalsIgnoreCase(cuenta)) {
                return c;
            }
        }
        return new Cuenta();
    }

    public boolean saldoSuficiente(Cuenta cuenta, PlazoFijo plazoFijo) {
        if (cuenta.getTipoDeCuenta().equalsIgnoreCase("Ahorro")) {
            return cuenta.getSaldo() >= plazoFijo.getMonto() ? true : false;
        } else {
            double saldo = cuenta.getSaldo() + cuenta.getAcuerdo();
            return saldo >= plazoFijo.getMonto() ? true : false;
        }
    }


    public List<PlazoFijoDto> listarPlazos(Cliente cliente){
        List<PlazoFijoDto> listaplazos= new ArrayList<>();
        for (PlazoFijo pf: cliente.getPlazoFijoLista()){
            PlazoFijoDto pfn= new PlazoFijoDto();
            pfn.setCapital(pf.getCapital());
            pfn.setIntereses(pf.getIntereses());
            pfn.setFechaVencimiento(pf.getFechaVencimiento());
            listaplazos.add(pfn);
        }
        return listaplazos;
    }


    public String getMontoTotal(List<PlazoFijoDto> lista) {
        DecimalFormat df = new DecimalFormat("0.00");
        double montoT=0;
        for (PlazoFijoDto pf: lista){
            montoT+=pf.getCapital();
        }
        return df.format(montoT);
    }
}
