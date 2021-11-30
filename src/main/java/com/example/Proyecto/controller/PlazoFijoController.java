package com.example.Proyecto.controller;

import com.example.Proyecto.configuration.GeneralsExceptions;
import com.example.Proyecto.dto.PlazoFijoDto;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.entity.PlazoFijo;
import com.example.Proyecto.repository.PlazoFijoRepository;

import net.bytebuddy.asm.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlazoFijoController {

    @Autowired
    PlazoFijoRepository plazoFijoRepository;

    @Autowired
    RestTemplate restTemplate;


    @PostMapping("/CrearPlazoFijo/{usuario}/{tipoC}")
    public ResponseEntity<Cliente> crearPlazoFijo(@PathVariable("usuario") String usuario, @PathVariable("tipoC") String tipocuenta, @RequestBody PlazoFijo plazoFijo) {

        if (!islogged(usuario)) {
            return new ResponseEntity("Primero debes Iniciar sesion", HttpStatus.BAD_REQUEST);
        }
        Cliente cliente = plazoFijoRepository.consumirCliente(usuario);

        if (cliente.getUsuario() != null) {
            Cuenta cuenta = plazoFijoRepository.tieneCuenta(cliente, tipocuenta);
            if (cuenta.getTipoDeCuenta() !=null) {
                if (plazoFijoRepository.saldoSuficiente(cuenta, plazoFijo)) {
                   Cliente clienteConPlazoFijo= plazoFijoRepository.crearPlazoFijo(cliente, plazoFijo, cuenta);
                    plazoFijoRepository.enviarDatosCliente(clienteConPlazoFijo);
                   return ResponseEntity.ok(clienteConPlazoFijo);

                } else {
                    return new ResponseEntity("No tienes el saldo suficinete para realizar la transaccion", HttpStatus.BAD_REQUEST);
                }

                //{monto, plazoDias, /cuenta, /tasa, /fechaVencimiento, capital, intereses}
            } else {
                return new ResponseEntity("No cuentas con una cuenta del tipo indicado", HttpStatus.BAD_REQUEST);
            }

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/consultarTazas")
    public ResponseEntity<List<String>> consultarTazas() {
        return ResponseEntity.ok(plazoFijoRepository.getTasas());
    }


    @GetMapping("/ListarPlazosFijos/{id}")
    public ResponseEntity<List<PlazoFijoDto>> listarPlazosFijos(@PathVariable String id) throws GeneralsExceptions {

        if (!islogged(id)) {
            return new ResponseEntity("Primero debes Iniciar sesion", HttpStatus.BAD_REQUEST);
        }

        Cliente cliente = plazoFijoRepository.consumirCliente(id);

        if (cliente.getUsuario() != null) {
            if (cliente.getPlazoFijoLista().size() > 0) {
                List<PlazoFijoDto> lista= plazoFijoRepository.listarPlazos(cliente);
                String montoT=plazoFijoRepository.getMontoTotal(lista);
                return ResponseEntity.ok(lista);
                //return ;
                 //ResponseEntity.ok(cliente.getPlazoFijoLista());
            } else {
                return new ResponseEntity(" El cliente no tiene plazos fijos", HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public boolean islogged(String usuario) {
        Boolean isLogged = restTemplate.getForObject("http://localhost:8080/islogged/" + usuario, Boolean.class);
        System.out.println(isLogged.booleanValue());
        return isLogged;
    }


}
