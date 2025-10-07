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
import java.util.ArrayList;
import java_cup.runtime.*;

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
            Files.move(Paths.get(origen), Paths.get(destino), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo movido con éxito!");
        } catch (IOException e) {
            System.out.println("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    public static void generateFiles()throws Exception{
        //Cambiar a ruta relativa
       
        String basePath = System.getProperty("user.dir");  // raíz del proyecto

        String rutaLexerEliminar = basePath + "/src/codigo/Lexer.java";
        String rutaLexerCrear = basePath + "/src/codigo/Lexer.flex";

        String rutaOriginalSym = basePath + "/sym.java";
        String rutaOriginalParser = basePath + "/parser.java";

        String rutaMoverParser = basePath + "/src/codigo/parser.java";
        String rutaMoverSym = basePath + "/src/codigo/sym.java";

        String rutaParserCrear = basePath + "/src/codigo/nuevo.cup";

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
    
    public static void probarParser(String archivoEntrada) {
        String basePath = System.getProperty("user.dir");
        
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

            ArrayList<Token> tokens = new ArrayList<>(); //CAMBIO
            String logTokens = archivoEntrada.replace(".txt", "_tokens.log");
            Lexer lexer = new Lexer(reader, logTokens, tokens);

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
            
            System.out.println("\n=== Crear tabla de símbolos ===");
            ArrayList<TablaDeSimbolos> tablas = crearTablasDeSimbolos(tokens);
            
            String texto = "";
            
            //Escribirlas en archivo
            for (TablaDeSimbolos tabla : tablas) {
                texto += tabla.toString();
            }
            
            escribirEnArchivo(texto, "tablaDeSimbolos.txt");
            
            //Mover el archivo
            moveFile(basePath + "/tablaDeSimbolos.txt", basePath + "/src/codigo/tablaDeSimbolos.txt");

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
    
    /*
        Leo cada elemento. Primero voy con las globales y van en esa lista. Luego voy reconociendo cada función y creo una tabla para cada variable que aparezca
    */
    private static ArrayList<TablaDeSimbolos> crearTablasDeSimbolos(ArrayList<Token> tokens){
        ArrayList<TablaDeSimbolos> tablasDeSimbolos = new ArrayList<>();
        ArrayList<Integer> pila = new ArrayList<>(); 
        boolean global = true;
        boolean noAgregarPila = false;
        Token tokenActual;
        TablaDeSimbolos tablaActual = new TablaDeSimbolos("Global"); 
        
        for (int i = 0; i<tokens.size(); i++) {
            tokenActual = tokens.get(i);
            //Primero intento reconocer variables globales
            if(global){
                if(tokenActual.nombre.equals("LET")){
                    //Debo de obtener el siguiente token el cual es el tipo y el que sigue que sería el identificador
                    tablaActual.agregarSimbolo(new Simbolo(tokens.get(i+2).lexema, tokens.get(i+1).lexema, tokens.get(i+2).linea, tokens.get(i+2).columna));
                }
                //Debo de reconocer hasta que encuentre el void ya que ese es la primera funcion
                if(tokenActual.nombre.equals("VOID")){
                    //La tabla de globales pasa a la lista
                    tablasDeSimbolos.add(tablaActual);
                    
                    //Bajo la bandera para que ya no busque globales
                    global = false;
                }
            }else{
                //Si la pila está vacía y encuentro un tipo significa que es una función
            if(pila.size() < 1 && (tokenActual.nombre.equals("VOID") || tokenActual.nombre.equals("INT") || tokenActual.nombre.equals("FLOAT") || tokenActual.nombre.equals("BOOL") || tokenActual.nombre.equals("CHAR") || tokenActual.nombre.equals("STRING") || tokenActual.nombre.equals("PRINCIPAL")) ){
                //Creo la tabla con el nombre de la función
                if(tokenActual.nombre.equals("PRINCIPAL")){
                    tablaActual = new TablaDeSimbolos("Principal");
                }else{
                    tablaActual = new TablaDeSimbolos(tokens.get(i+1).lexema);
                }
                
   
                
                //Agrego un a la pila y levanto una bandera para no poner doble
                pila.add(1);
                noAgregarPila = true;
            }else{
                //Caso donde tengo algo en la pila que significa que estoy dentro de alguna función
                if(pila.size() > 0){
                    if(tokenActual.nombre.equals("LEFT_BLOCK") && noAgregarPila){
                        //Bajo la bandera
                        noAgregarPila = false;
                    }else{
                        if(tokenActual.nombre.equals("LEFT_BLOCK") && !noAgregarPila){
                        //Agrego a la pila
                        pila.add(1);
                    }

                    if(tokenActual.nombre.equals("RIGHT_BLOCK")){
                        //Debo de desapilar y verificar si ya se quedó vacía
                        pila.remove(0);

                        if(pila.size() < 1){
                            //Guardo esa tabla de símbolos porque ya recorrí toda la función
                            tablasDeSimbolos.add(tablaActual);
                        }
                    }

                    //Reconocer identificadores
                    if(tokenActual.nombre.equals("LET")){
                        //Debo de obtener el siguiente token el cual es el tipo y el que sigue que sería el identificador
                        tablaActual.agregarSimbolo(new Simbolo(tokens.get(i+2).lexema, tokens.get(i+1).lexema, tokens.get(i+2).linea, tokens.get(i+2).columna));
                    }
                    }


                }
                }   
            }   
        }
        return tablasDeSimbolos;
    }
    
    private static void escribirEnArchivo(String pEntrada, String pArchivo) {
        try (FileWriter writer = new FileWriter(pArchivo)) { 
            writer.write(pEntrada);
            writer.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
