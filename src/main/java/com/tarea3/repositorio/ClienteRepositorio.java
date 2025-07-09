package com.tarea3.repositorio;

import com.tarea3.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositorio {
    private final List<Cliente> clientes;
    private int siguienteId;
    
    public ClienteRepositorio() {
        this.clientes = new ArrayList<>();
        this.siguienteId = 1;
    }
    
    public int generarSiguienteId() {
        return siguienteId++;
    }
    
    public Cliente guardar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        if (existePorId(cliente.getId())) {
            throw new IllegalArgumentException("Ya existe un cliente con ID " + cliente.getId());
        }
        
        clientes.add(cliente);
        return cliente;
    }
    
    public Optional<Cliente> buscarPorId(int id) {
        return clientes.stream()
                .filter(cliente -> cliente.getId() == id)
                .findFirst();
    }
    
    public Optional<Cliente> buscarPorCorreo(String correo) {
        if (correo == null) {
            return Optional.empty();
        }
        
        return clientes.stream()
                .filter(cliente -> cliente.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }
    
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }
    
    public Cliente actualizar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        int index = -1;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            throw new IllegalArgumentException("No existe un cliente con ID " + cliente.getId());
        }
        
        clientes.set(index, cliente);
        return cliente;
    }
    
    public boolean eliminar(int id) {
        return clientes.removeIf(cliente -> cliente.getId() == id);
    }
    
    public boolean existePorId(int id) {
        return clientes.stream()
                .anyMatch(cliente -> cliente.getId() == id);
    }
    
    public boolean existePorCorreo(String correo) {
        if (correo == null) {
            return false;
        }
        
        return clientes.stream()
                .anyMatch(cliente -> cliente.getCorreo().equalsIgnoreCase(correo));
    }
    
    public int contar() {
        return clientes.size();
    }
    
    public void limpiar() {
        clientes.clear();
    }
}