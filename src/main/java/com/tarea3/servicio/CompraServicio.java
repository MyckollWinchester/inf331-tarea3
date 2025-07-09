package com.tarea3.servicio;

import com.tarea3.modelo.Cliente;
import com.tarea3.modelo.Compra;
import com.tarea3.repositorio.CompraRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompraServicio {
    private final CompraRepositorio compraRepositorio;
    
    public CompraServicio(CompraRepositorio compraRepositorio) {
        this.compraRepositorio = compraRepositorio;
    }
    
    public Compra registrarCompra(int idCliente, int monto, LocalDate fecha) {
        int idCompra = compraRepositorio.generarSiguienteId();
        Compra compra = new Compra(idCompra, idCliente, monto, fecha);
        return compraRepositorio.guardar(compra);
    }
    
    public Compra registrarCompra(int idCompra, int idCliente, int monto, LocalDate fecha) {
        Compra compra = new Compra(idCompra, idCliente, monto, fecha);
        return compraRepositorio.guardar(compra);
    }
    
    public Optional<Compra> buscarPorId(int id) {
        return compraRepositorio.buscarPorId(id);
    }
    
    public List<Compra> obtenerTodas() {
        return compraRepositorio.obtenerTodas();
    }
    
    public List<Compra> obtenerPorCliente(int idCliente) {
        return compraRepositorio.obtenerPorCliente(idCliente);
    }
    
    public boolean eliminarCompra(int id) {
        return compraRepositorio.eliminar(id);
    }
    
    public int procesarCompra(Cliente cliente, int monto, LocalDate fecha) {
        Compra compra = registrarCompra(cliente.getId(), monto, fecha);
        List<Compra> historial = obtenerPorCliente(cliente.getId());
        return calcularPuntosConBonus(compra, cliente, historial);
    }
    
    public int procesarCompra(int idCompra, Cliente cliente, int monto, LocalDate fecha) {
        Compra compra = registrarCompra(idCompra, cliente.getId(), monto, fecha);
        List<Compra> historial = obtenerPorCliente(cliente.getId());
        return calcularPuntosConBonus(compra, cliente, historial);
    }
    
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
    
    public boolean debeAplicarBonus(Compra compraNueva, Cliente cliente, List<Compra> historialCompras) {
        if (compraNueva == null || cliente == null || historialCompras == null) {
            return false;
        }
        
        LocalDate fechaCompra = compraNueva.getFecha();
        
        List<Compra> comprasDelDia = historialCompras.stream()
            .filter(compra -> compra.getIdCliente() == cliente.getId())
            .filter(compra -> compra.esMismoDia(fechaCompra))
            .filter(compra -> compra.getIdCompra() != compraNueva.getIdCompra()) // Excluir la compra actual
            .collect(Collectors.toList());
        
        if (comprasDelDia.size() >= 2) {
            return !yaSeAplicoBonusEnElDia(fechaCompra, cliente, historialCompras);
        }
        
        return false;
    }
    
    private boolean yaSeAplicoBonusEnElDia(LocalDate fecha, Cliente cliente, List<Compra> historialCompras) {
        long comprasDelDia = historialCompras.stream()
            .filter(compra -> compra.getIdCliente() == cliente.getId())
            .filter(compra -> compra.esMismoDia(fecha))
            .count();
        
        return comprasDelDia >= 3;
    }
    
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