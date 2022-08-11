/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread {
    
    int a = 0;
    int b = 0;
    String nombreHilo = null;
    
    @Override
    public void run() {        
        for (int i = a; i < b; i++) {
            System.out.println(i+" "+nombreHilo);
        }
    }

    /**
     * Metodo que retorna el nombre del hilo
     * @return nombre del hilo
     */
    public String getNombreHilo() {
        return nombreHilo;
    }

    /**
     * Metodo que cambia el nombre del hilo
     * @param nombreHilo nombre del hilo
     */
    public void setNombreHilo(String nombreHilo) {
        this.nombreHilo = nombreHilo;
    }


    /**
     * Metodo get del numero A
     * @return int A
     */
    public int getA() {
        return a;
    }

    /**
     * Metodo que cambia el valor A
     * @param a valor nuevo de A
     */
    public void setA(int a) {
        this.a = a;
    }

    /**
     * Metodo get del numero B
     * @return int B
     */
    public int getB() {
        return b;
    }

    /**
     * Metodo que cambia el valor B
     * @param b valor nuevo de B
     */
    public void setB(int b) {
        this.b = b;
    }
    
    
}
