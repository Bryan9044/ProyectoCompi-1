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
             
        String rutaParserCrear = "C:/Users/dylan/OneDrive - Estudiantes ITCR/Compiladores/Proyectos/Primer Proyecto/ProyectoCompi-1/Analizador/src/codigo/parser.cup";
        
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
    
    
}
