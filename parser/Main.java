import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== INICIANDO ANALISIS ===\n");
        
        LexerReal lexer = new LexerReal(new FileReader("prueba.txt"));
        parser p = new parser(lexer);
        
        System.out.println("Leyendo tokens...\n");
        p.parse();
        
        System.out.println("\n=== ANALISIS COMPLETADO EXITOSAMENTE ===");
    }
}