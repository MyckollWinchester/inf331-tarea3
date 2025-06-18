package com.tarea3.modelo;

import java.util.Objects;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Cliente {
    private int id;
    private String nombre;
    private String correo;
    private int puntos;
    private Nivel nivel;
    private int streakDias;
    
    public Cliente(int id, String nombre, String correo) {
        validarDatos(id, nombre, correo);
        
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
        this.streakDias = 0;
    }
    
    private void validarDatos(int id, String nombre, String correo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (!esEmailValido(correo)) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
    }

    private boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }
    
    public void agregarPuntos(int puntosNuevos) {
        if (puntosNuevos < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }
        
        this.puntos += puntosNuevos;
        this.nivel = Nivel.calcularNivel(this.puntos);
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public int getPuntos() { return puntos; }
    public Nivel getNivel() { return nivel; }
    public int getStreakDias() { return streakDias; }
    
    // Setters para actualización
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
    
    public void setCorreo(String correo) {
        if (correo == null || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo debe ser válido (contener @)");
        }
        this.correo = correo;
    }
    
    public void setStreakDias(int streakDias) {
        this.streakDias = Math.max(0, streakDias);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Cliente{id=%d, nombre='%s', correo='%s', puntos=%d, nivel=%s, streak=%d}", 
                           id, nombre, correo, puntos, nivel, streakDias);
    }
}