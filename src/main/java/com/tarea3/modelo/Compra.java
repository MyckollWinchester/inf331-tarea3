package com.tarea3.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Compra {
    private int idCompra;
    private int idCliente;
    private int monto;
    private LocalDate fecha;
    
    public Compra(int idCompra, int idCliente, int monto, LocalDate fecha) {
        validarDatos(idCompra, idCliente, monto, fecha);
        
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }
    
    private void validarDatos(int idCompra, int idCliente, int monto, LocalDate fecha) {
        if (idCompra <= 0) {
            throw new IllegalArgumentException("El ID de compra debe ser mayor a 0");
        }
        
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser mayor a 0");
        }
        
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
    }
    
    public int calcularPuntosBase() {
        return (int) Math.floor(monto / 100);
    }
    
    public int calcularPuntosTotales(Cliente cliente) {
        return calcularPuntosTotales(cliente, false);
    }
    
    public int calcularPuntosTotales(Cliente cliente, boolean aplicarBonus) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        if (cliente.getId() != this.idCliente) {
            throw new IllegalArgumentException("El ID del cliente no coincide con el ID de la compra");
        }
        
        int puntosBase = calcularPuntosBase();
        int puntosBonus = aplicarBonus ? 10 : 0;
        double multiplicador = cliente.getNivel().getMultiplicador();
        
        return (int) Math.floor((puntosBase + puntosBonus) * multiplicador);
    }
    
    public boolean esMismoDia(LocalDate otraFecha) {
        if (otraFecha == null) {
            return false;
        }
        return this.fecha.equals(otraFecha);
    }
    
    // Getters
    public int getIdCompra() {
        return idCompra;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public int getMonto() {
        return monto;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    // Setters para actualización
    public void setMonto(int monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.monto = monto;
    }
    
    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }
        
        this.fecha = fecha;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return idCompra == compra.idCompra;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idCompra);
    }
    
    @Override
    public String toString() {
        return String.format("Compra{idCompra=%d, idCliente=%d, monto=%d, fecha=%s}", 
                           idCompra, idCliente, monto, fecha);
    }
}