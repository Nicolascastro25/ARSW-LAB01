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
public class CountThreadsMain {
    
    public static void main(String a[]){
        CountThread ct = new CountThread();
        CountThread cta = new CountThread();
        CountThread cth = new CountThread();
        ct.setA(0);
        ct.setB(99);
        ct.setNombreHilo("hilo 1");
        cta.setA(99);
        cta.setB(199);
        cta.setNombreHilo("hilo 2");
        cth.setA(200);
        cth.setB(299);
        cth.setNombreHilo("hilo 3");
        ct.run();
        cta.run();
        cth.run();
    }
    
}
