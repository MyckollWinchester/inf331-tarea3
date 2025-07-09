package com.tarea3.servicio;

import com.tarea3.modelo.Cliente;
import com.tarea3.repositorio.ClienteRepositorio;
import java.util.List;
import java.util.Optional;

public class ClienteServicio {
    private final ClienteRepositorio clienteRepositorio;
    
    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }
    
    public Cliente crearCliente(String nombre, String correo) {
        if (clienteRepositorio.existePorCorreo(correo)) {
            throw new IllegalArgumentException("Ya existe un cliente con el correo " + correo);
        }
        
        int id = clienteRepositorio.generarSiguienteId();
        Cliente cliente = new Cliente(id, nombre, correo);
        return clienteRepositorio.guardar(cliente);
    }
    
    public Cliente crearCliente(int id, String nombre, String correo) {
        if (clienteRepositorio.existePorCorreo(correo)) {
            throw new IllegalArgumentException("Ya existe un cliente con el correo " + correo);
        }
        
        Cliente cliente = new Cliente(id, nombre, correo);
        return clienteRepositorio.guardar(cliente);
    }
    
    public Optional<Cliente> buscarPorId(int id) {
        return clienteRepositorio.buscarPorId(id);
    }
    
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepositorio.buscarPorCorreo(correo);
    }
    
    public List<Cliente> obtenerTodos() {
        return clienteRepositorio.obtenerTodos();
    }
    
    public Cliente actualizarNombre(int id, String nuevoNombre) {
        Optional<Cliente> clienteOpt = clienteRepositorio.buscarPorId(id);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un cliente con ID " + id);
        }
        
        Cliente cliente = clienteOpt.get();
        cliente.setNombre(nuevoNombre);
        return clienteRepositorio.actualizar(cliente);
    }
    
    public Cliente actualizarCorreo(int id, String nuevoCorreo) {
        Optional<Cliente> clienteOpt = clienteRepositorio.buscarPorId(id);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un cliente con ID " + id);
        }
        
        Cliente cliente = clienteOpt.get();
        
        Optional<Cliente> clienteConCorreo = clienteRepositorio.buscarPorCorreo(nuevoCorreo);
        if (clienteConCorreo.isPresent() && clienteConCorreo.get().getId() != id) {
            throw new IllegalArgumentException("Ya existe un cliente con el correo " + nuevoCorreo);
        }
        
        cliente.setCorreo(nuevoCorreo);
        return clienteRepositorio.actualizar(cliente);
    }
    
    public Cliente agregarPuntos(int id, int puntos) {
        Optional<Cliente> clienteOpt = clienteRepositorio.buscarPorId(id);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un cliente con ID " + id);
        }
        
        Cliente cliente = clienteOpt.get();
        cliente.agregarPuntos(puntos);
        return clienteRepositorio.actualizar(cliente);
    }
    
    public boolean eliminarCliente(int id) {
        return clienteRepositorio.eliminar(id);
    }
    
    public boolean existeCliente(int id) {
        return clienteRepositorio.existePorId(id);
    }
    
    public int contarClientes() {
        return clienteRepositorio.contar();
    }
}