/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

/**
 *
 * @author dylan
 */
public class Principal {
    public static void main(String[] args)throws Exception{
        String basePath = System.getProperty("user.dir");
        String rutaTxt = basePath + "/src/codigo/pruebaParser.txt";
        
        //JflexCup.generateFiles();
        JflexCup.probarParser(rutaTxt);
    }
}
