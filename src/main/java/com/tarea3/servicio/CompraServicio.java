package com.tarea3.servicio;

import com.tarea3.modelo.Cliente;
import com.tarea3.modelo.Compra;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio relacionada con compras
 * incluyendo el cálculo de bonus streak
 */
public class CompraServicio {
    
    /**
     * Calcula los puntos que debe recibir un cliente por una compra,
     * considerando el bonus streak si aplica
     * 
     * @param compra la compra realizada
     * @param cliente el cliente que realizó la compra
     * @param historialCompras historial de compras del cliente
     * @return puntos totales a otorgar
     */
    public int calcularPuntosConBonus(Compra compra, Cliente cliente, List<Compra> historialCompras) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser nula");
        }
        
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        if (historialCompras == null) {
            throw new IllegalArgumentException("El historial de compras no puede ser nulo");
        }
        
        boolean aplicarBonus = debeAplicarBonus(compra, cliente, historialCompras);
        return compra.calcularPuntosTotales(cliente, aplicarBonus);
    }
    
    /**
     * Determina si se debe aplicar el bonus streak a una compra
     * Regla: +10 puntos por 3 compras seguidas en el mismo día (se aplica una sola vez por día)
     * 
     * @param compraNueva la nueva compra a evaluar
     * @param cliente el cliente
     * @param historialCompras historial de compras del cliente
     * @return true si se debe aplicar el bonus
     */
    public boolean debeAplicarBonus(Compra compraNueva, Cliente cliente, List<Compra> historialCompras) {
        if (compraNueva == null || cliente == null || historialCompras == null) {
            return false;
        }
        
        LocalDate fechaCompra = compraNueva.getFecha();
        
        // Obtener compras del mismo día (sin incluir la compra nueva)
        List<Compra> comprasDelDia = historialCompras.stream()
            .filter(compra -> compra.getIdCliente() == cliente.getId())
            .filter(compra -> compra.esMismoDia(fechaCompra))
            .filter(compra -> compra.getIdCompra() != compraNueva.getIdCompra()) // Excluir la compra actual
            .collect(Collectors.toList());
        
        // Si ya hay 2 compras en el día, la tercera (compraNueva) obtiene bonus
        // Pero solo si no se ha aplicado bonus ese día
        if (comprasDelDia.size() >= 2) {
            return !yaSeAplicoBonusEnElDia(fechaCompra, cliente, historialCompras);
        }
        
        return false;
    }
    
    /**
     * Verifica si ya se aplicó bonus streak en un día específico
     * 
     * @param fecha fecha a verificar
     * @param cliente el cliente
     * @param historialCompras historial de compras
     * @return true si ya se aplicó bonus ese día
     */
    private boolean yaSeAplicoBonusEnElDia(LocalDate fecha, Cliente cliente, List<Compra> historialCompras) {
        // Esta lógica se implementaría con información adicional sobre qué compras
        // ya recibieron bonus. Por simplicidad, asumimos que solo se puede aplicar
        // una vez cuando se alcanza exactamente la tercera compra del día.
        
        long comprasDelDia = historialCompras.stream()
            .filter(compra -> compra.getIdCliente() == cliente.getId())
            .filter(compra -> compra.esMismoDia(fecha))
            .count();
        
        // Si hay 3 o más compras, asumimos que ya se aplicó el bonus
        return comprasDelDia >= 3;
    }
    
    /**
     * Cuenta las compras de un cliente en una fecha específica
     * 
     * @param cliente el cliente
     * @param fecha la fecha
     * @param historialCompras historial de compras
     * @return número de compras en esa fecha
     */
    public long contarComprasEnFecha(Cliente cliente, LocalDate fecha, List<Compra> historialCompras) {
        if (cliente == null || fecha == null || historialCompras == null) {
            return 0;
        }
        
        return historialCompras.stream()
            .filter(compra -> compra.getIdCliente() == cliente.getId())
            .filter(compra -> compra.esMismoDia(fecha))
            .count();
    }
}