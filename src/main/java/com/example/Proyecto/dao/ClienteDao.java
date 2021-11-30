package com.example.Proyecto.dao;

import com.example.Proyecto.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteDao extends CrudRepository<Cliente, String> {
}
