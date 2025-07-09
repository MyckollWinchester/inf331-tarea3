package com.tarea3;

import com.tarea3.cli.MenuPrincipal;
import com.tarea3.cli.ConsoleUtils;
import com.tarea3.repositorio.ClienteRepositorio;
import com.tarea3.repositorio.CompraRepositorio;
import com.tarea3.servicio.ClienteServicio;
import com.tarea3.servicio.CompraServicio;

public class App {
    public static void main(String[] args) {
        try {
            // almacenamiento en memoria
            ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
            CompraRepositorio compraRepositorio = new CompraRepositorio();
            
            // servicios
            ClienteServicio clienteServicio = new ClienteServicio(clienteRepositorio);
            CompraServicio compraServicio = new CompraServicio(compraRepositorio);
            
            // para iniciar con datos de ejemplo usar --demo al ejecutar el jar
            if (args.length > 0 && args[0].equals("--demo")) {
                crearDatosDemo(clienteServicio, compraServicio);
            }
            
            MenuPrincipal menuPrincipal = new MenuPrincipal(clienteServicio, compraServicio);
            menuPrincipal.ejecutar();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConsoleUtils.cerrar();
        }
    }
    
    private static void crearDatosDemo(ClienteServicio clienteServicio, CompraServicio compraServicio) {
        try {
            clienteServicio.crearCliente(1, "Juan Pérez", "juan.perez@email.com");
            clienteServicio.crearCliente(2, "María González", "maria.gonzalez@email.com");
            clienteServicio.crearCliente(3, "Carlos Rodríguez", "carlos.rodriguez@email.com");
            
            clienteServicio.agregarPuntos(1, 300);  // bronce
            clienteServicio.agregarPuntos(2, 800);  // plata
            clienteServicio.agregarPuntos(3, 2000); // oro
            
            compraServicio.registrarCompra(1, 1, 150, java.time.LocalDate.now().minusDays(5));
            compraServicio.registrarCompra(2, 1, 200, java.time.LocalDate.now().minusDays(3));
            compraServicio.registrarCompra(3, 2, 300, java.time.LocalDate.now().minusDays(2));
            compraServicio.registrarCompra(4, 3, 500, java.time.LocalDate.now().minusDays(1));
        } catch (Exception e) {
            System.err.println("Error creando datos de demostración: " + e.getMessage());
        }
    }
}