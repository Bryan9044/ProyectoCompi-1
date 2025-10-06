/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java_cup.runtime.Symbol;

/**
 *
 * @author dylan
 */
public class JflexCup {
    private static void generateParser(String ruta)throws Exception{
        String[] strArr = {ruta};
        java_cup.Main.main(strArr);
    }
    
    private static void generateLexer(String ruta)throws Exception{
        String[] strArr = {ruta};
        jflex.Main.generate(strArr);
    }
    
    private static void deleteFile(String ruta)throws Exception{
       Path rutaArchivo = Paths.get(ruta);
       if(Files.exists(rutaArchivo)){
           Files.delete(rutaArchivo);
       } 
    }
    
    private static void moveFile(String origen, String destino){
        
        try {
            // Mueve el archivo. Si ya existe en destino, lo reemplaza.
            Files.move(Paths.get(origen), Paths.get(destino), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo movido con éxito!");
        } catch (IOException e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public static void generateFiles()throws Exception{
        
        //Cambiar a ruta relativa
        String rutaLexerEliminar = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/Lexer.java";
        String rutaLexerCrear = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/Lexer.flex";
        
        String rutaOriginalSym = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/sym.java";
        String rutaOriginalParser = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/parser.java";
        
        String rutaMoverParser ="C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/parser.java";
        String rutaMoverSym = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/sym.java";
             
        String rutaParserCrear = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/nuevo.cup";
        
        //Borar los archivos
        deleteFile(rutaLexerEliminar);
        deleteFile(rutaMoverParser);
        deleteFile(rutaMoverSym);
        deleteFile(rutaOriginalSym);
        deleteFile(rutaOriginalParser);
        
        //Generar los nuevos
        generateLexer(rutaLexerCrear);
        generateParser(rutaParserCrear);
        
        //Mover los archivos del sym y parser a esta carpeta
        moveFile(rutaOriginalSym, rutaMoverSym);
        moveFile(rutaOriginalParser, rutaMoverParser);
    }
    
    public static void test()throws Exception{
        System.out.println("=== INICIANDO ANALISIS ===\n");
        
        Lexer lexer = new Lexer(new FileReader("C:/Users/dylan/OneDrive/Documentos/NetBeansProjects/Analizador/src/codigo/prueba.txt"));
        parser p = new parser(lexer);
        
        System.out.println("Leyendo tokens...\n");
        p.parse();
        
        System.out.println("\n=== ANALISIS COMPLETADO EXITOSAMENTE ===");
    }
    
    public static void testearParser(String archivoEntrada) {
        try {
            System.out.println("    Probando parser o sea CUP");
            
            System.out.println("Archivo de entrada: " + archivoEntrada);
            System.out.println();
            
            // Crear lexer con encoding UTF-8
            FileInputStream fis = new FileInputStream(archivoEntrada);
            InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            Lexer lexer = new Lexer(reader, "C:/Users/bryan/Documents/Analizador/src/codigo/prueba.txt");
            
            // Crear parser
            System.out.println("--- INICIANDO ANALISIS SINTACTICO ---\n");
            parser parser = new parser(lexer);
            
            // Parsear
            Symbol resultado = parser.parse();
            
            // Si llega aquí, fue exitoso
            System.out.println("     PARSER FUNCIONA CORRECTAMENTE ");
            System.out.println("Sintaxis valida");
            System.out.println("Estructura correcta");
            System.out.println("\nTokens guardados en: tokens_test.txt");
            
        } catch (FileNotFoundException e) {
            System.err.println("\n ERROR: No se encontro el archivo: " + archivoEntrada);
        } catch (Exception e) {
            System.err.println("     ERROR EN EL PARSER");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    
    
    public static void probarParser(String archivoEntrada) {
        System.out.println("║     PRUEBA DEL PARSER CUP              ║");
        System.out.println("\n Archivo: " + archivoEntrada);
        System.out.println("\n" + "=".repeat(45));

        try {
            File archivo = new File(archivoEntrada);
            if (!archivo.exists()) {
                System.err.println(" ERROR: El archivo no existe");
                System.err.println("   Ruta: " + archivo.getAbsolutePath());
                return;
            }

            System.out.println("\n CONTENIDO DEL ARCHIVO:");
            System.out.println("-".repeat(45));
            mostrarArchivo(archivoEntrada);
            System.out.println("-".repeat(45));

            FileInputStream fis = new FileInputStream(archivoEntrada);
            InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            // ← FIX: Generar nombre del archivo de log correctamente
            String logTokens = archivoEntrada.replace(".txt", "_tokens.log");
            Lexer lexer = new Lexer(reader, logTokens);

            System.out.println("\n Empieza analisis...\n");
            parser parser = new parser(lexer);

            Symbol resultado = parser.parse();

            System.out.println("\n" + "=".repeat(45));
            System.out.println("\n¡Parser funciona!\n");
            System.out.println("Análisis léxico: OK");
            System.out.println("Análisis sintáctico: OK");
            System.out.println("Estructura del programa: VÁLIDA");
            System.out.println("\n Log de tokens en: " + logTokens);
            System.out.println("\n" + "=".repeat(45));

        } catch (FileNotFoundException e) {
            System.err.println("\n❌ ERROR: Archivo no encontrado");
            System.err.println("   " + e.getMessage());

        } catch (Exception e) {
            System.err.println("\n" + "=".repeat(45));
            System.err.println("ERROR EN EL ANÁLISIS");
            System.err.println("=".repeat(45));
            System.err.println("\n" + e.getMessage());
            System.err.println("\n📍 Stack trace:");
            e.printStackTrace();
        }
    }
    
    private static void mostrarArchivo(String ruta) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {
            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                System.out.printf("%3d │ %s\n", numLinea++, linea);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
        }
    }
    
    
}
