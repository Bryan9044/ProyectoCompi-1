/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

/**
 *
 * @author dylan
 */
public class Simbolo {
    private String simbolo;
    private String tipo;
    private int fila;
    private int columna;
    
    public Simbolo(String pSimbolo, String pTipo, int pFila, int pColumna){
        this.simbolo = pSimbolo;
        this.tipo = pTipo;
        this.fila = pFila;
        this.columna = pColumna;
    }
    
@Override
   public String toString(){
       return "Simbolo: " + simbolo + 
           "\tTipo: " + tipo +
           "\tLinea: " + fila +
           "\tColumna: " + columna;
           
   }
}

