package codigo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.io.IOException;
import java.io.*;
import java_cup.runtime.Symbol;

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
        String rutaLexerEliminar = "C:/Users/bryan/Documents/Analizador/src/codigo/Lexer.java";
        String rutaLexerCrear = "C:/Users/bryan/Documents/Analizador/src/codigo/Lexer.flex";
        String rutaOriginalSym = "C:/Users/bryan/Documents/Analizador/sym.java";
        String rutaOriginalParser = "C:/Users/bryan/Documents/Analizador/parser.java";
        String rutaMoverParser ="C:/Users/bryan/Documents/Analizador/src/codigo/parser.java";
        String rutaMoverSym = "C:/Users/bryan/Documents/Analizador/src/codigo/sym.java";
        String rutaParserCrear = "C:/Users/bryan/Documents/Analizador/src/codigo/nuevo.cup";
        deleteFile(rutaLexerEliminar);
        deleteFile(rutaMoverParser);
        deleteFile(rutaMoverSym);
        deleteFile(rutaOriginalSym);
        deleteFile(rutaOriginalParser);
        generateLexer(rutaLexerCrear);
        generateParser(rutaParserCrear);
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
    
    
    public static void analizarArchivo(String inputFile) {
        try {
            String outputFile = inputFile.replace(".txt", "_tokens.txt");

            Lexer lexer = new Lexer(new FileReader(inputFile));
            parser parser = new parser(lexer);

            System.out.println("=== INICIANDO ANALISIS ===\n");
            System.out.println("Leyendo tokens...");

            try (BufferedWriter out = new BufferedWriter(new FileWriter(outputFile))) {
                Symbol token;
                while ((token = lexer.next_token()).sym != sym.EOF) {
                    String nombreToken = sym.terminalNames[token.sym]; // nombre del token
                    out.write(nombreToken + " -> " + token.value);
                    out.newLine();
                }
            }

            System.out.println("\n=== ANALISIS COMPLETADO EXITOSAMENTE ===");
            System.out.println("Tokens guardados en: " + outputFile);

        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
