package com.example.Proyecto.repository;

import com.example.Proyecto.dao.ClienteDao;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.PlazoFijo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class ClienteRepository {

    @Autowired
    private ClienteDao clienteDao;



    public List<PlazoFijo> getPlazosFijo(Cliente cliente){
        return (List<PlazoFijo>) clienteDao.findById(cliente.getUsuario()).get().getPlazoFijoLista();
    }



}
