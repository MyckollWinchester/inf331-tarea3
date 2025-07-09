package com.tarea3.cli;

import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

    // ansi escape codes para que quede monono
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    public static final String BOLD = "\u001B[1m";
    public static final String CLEAR_SCREEN = "\u001B[2J\u001B[H";
    
    public static String leerString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(YELLOW + "[!] La entrada no puede estar vacía. Inténtelo nuevamente." + RESET);
            }
        } while (input.isEmpty());
        return input;
    }
    
    public static int leerEntero(String prompt) {
        int numero;
        while (true) {
            System.out.print(prompt);
            try {
                numero = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println(YELLOW + "[!] Por favor ingrese un número valido." + RESET);
            }
        }
        return numero;
    }
    
    public static int leerEnteroPositivo(String prompt) {
        int numero;
        do {
            numero = leerEntero(prompt);
            if (numero <= 0) {
                System.out.println(YELLOW + "[!] El número debe ser mayor a 0." + RESET);
            }
        } while (numero <= 0);
        return numero;
    }
    
    public static int leerEnteroNoNegativo(String prompt) {
        int numero;
        do {
            numero = leerEntero(prompt);
            if (numero < 0) {
                System.out.println(YELLOW + "[!] El número no puede ser negativo." + RESET);
            }
        } while (numero < 0);
        return numero;
    }

    public static int leerOpcionMenu(String prompt, int min, int max) {
        int opcion;
        do {
            opcion = leerEntero(prompt);
            if (opcion < min || opcion > max) {
                System.out.println(YELLOW + "[!] Opción inválida. Seleccione entre " + min + " y " + max + "." + RESET);
            }
        } while (opcion < min || opcion > max);
        return opcion;
    }
    
    public static boolean confirmar(String prompt) {
        System.out.print(prompt + " (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si");
    }
    
    public static void pausar() {
        System.out.print("\n" + CYAN + "Presione Enter para continuar..." + RESET);
        scanner.nextLine();
    }
    
    public static void limpiarPantalla() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }
    
    public static void cerrar() {
        scanner.close();
    }
    
    public static String leerStringOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public static void mostrarExito(String mensaje) {
        System.out.println(GREEN + "[OK] " + mensaje + RESET);
    }
    
    public static void mostrarError(String mensaje) {
        System.out.println(RED + "[ERROR] " + mensaje + RESET);
    }
    
    public static void mostrarAdvertencia(String mensaje) {
        System.out.println(YELLOW + "[!] " + mensaje + RESET);
    }
    
    public static void mostrarTitulo(String titulo) {
        String espacios = " ".repeat(Math.max(0, (50 - titulo.length()) / 2));
        System.out.println(espacios + BLUE + BOLD + titulo + RESET);
    }
}
