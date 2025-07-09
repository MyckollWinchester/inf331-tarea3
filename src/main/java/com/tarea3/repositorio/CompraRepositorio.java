package com.tarea3.repositorio;

import com.tarea3.modelo.Compra;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompraRepositorio {
    private final List<Compra> compras;
    private int siguienteId;
    
    public CompraRepositorio() {
        this.compras = new ArrayList<>();
        this.siguienteId = 1;
    }
    
    public int generarSiguienteId() {
        return siguienteId++;
    }
    
    public Compra guardar(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser nula");
        }
        
        if (existePorId(compra.getIdCompra())) {
            throw new IllegalArgumentException("Ya existe una compra con ID " + compra.getIdCompra());
        }
        
        compras.add(compra);
        return compra;
    }
    
    public Optional<Compra> buscarPorId(int id) {
        return compras.stream()
                .filter(compra -> compra.getIdCompra() == id)
                .findFirst();
    }
    
    public List<Compra> obtenerTodas() {
        return new ArrayList<>(compras);
    }
    
    public List<Compra> obtenerPorCliente(int idCliente) {
        return compras.stream()
                .filter(compra -> compra.getIdCliente() == idCliente)
                .collect(Collectors.toList());
    }
    
    public List<Compra> obtenerPorClienteYFecha(int idCliente, LocalDate fecha) {
        if (fecha == null) {
            return new ArrayList<>();
        }
        
        return compras.stream()
                .filter(compra -> compra.getIdCliente() == idCliente)
                .filter(compra -> compra.esMismoDia(fecha))
                .collect(Collectors.toList());
    }
    
    public List<Compra> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }
        
        return compras.stream()
                .filter(compra -> !compra.getFecha().isBefore(fechaInicio) && 
                                 !compra.getFecha().isAfter(fechaFin))
                .collect(Collectors.toList());
    }
    
    public Compra actualizar(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser nula");
        }
        
        int index = -1;
        for (int i = 0; i < compras.size(); i++) {
            if (compras.get(i).getIdCompra() == compra.getIdCompra()) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            throw new IllegalArgumentException("No existe una compra con ID " + compra.getIdCompra());
        }
        
        compras.set(index, compra);
        return compra;
    }
    
    public boolean eliminar(int id) {
        return compras.removeIf(compra -> compra.getIdCompra() == id);
    }
    
    public int eliminarPorCliente(int idCliente) {
        int tamanoInicial = compras.size();
        compras.removeIf(compra -> compra.getIdCliente() == idCliente);
        return tamanoInicial - compras.size();
    }
    
    public boolean existePorId(int id) {
        return compras.stream()
                .anyMatch(compra -> compra.getIdCompra() == id);
    }
    
    public int contar() {
        return compras.size();
    }
    
    public int contarPorCliente(int idCliente) {
        return (int) compras.stream()
                .filter(compra -> compra.getIdCliente() == idCliente)
                .count();
    }
    
    public void limpiar() {
        compras.clear();
    }
}