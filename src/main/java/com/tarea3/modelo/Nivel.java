package com.tarea3.modelo;

public enum Nivel {
    BRONCE(0, 499, 1.0),
    PLATA(500, 1499, 1.2),
    ORO(1500, 2999, 1.5),
    PLATINO(3000, Integer.MAX_VALUE, 2.0);
    
    private final int minPuntos;
    private final int maxPuntos;
    private final double multiplicador;
    
    Nivel(int minPuntos, int maxPuntos, double multiplicador) {
        this.minPuntos = minPuntos;
        this.maxPuntos = maxPuntos;
        this.multiplicador = multiplicador;
    }
    
    public int getMinPuntos() {
        return minPuntos;
    }
    
    public int getMaxPuntos() {
        return maxPuntos;
    }
    
    public double getMultiplicador() {
        return multiplicador;
    }
    
    public static Nivel calcularNivel(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }
        for (Nivel nivel : values()) {
            if (puntos >= nivel.minPuntos && puntos <= nivel.maxPuntos) {
                return nivel;
            }
        }
        return BRONCE;
    }
}