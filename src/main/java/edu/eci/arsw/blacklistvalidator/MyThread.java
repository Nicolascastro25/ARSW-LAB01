/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class MyThread extends Thread {

    private final String ipAddress;
    private final HostBlacklistsDataSourceFacade skds;
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int checkedListCount;
    private final int firstServer,lastServer;
    private int ocurrencesCount;
    private final LinkedList<Integer> blackListOcurrences;

    /**
     * Constructor de la clase
     * @param ipAddress ip a buscar
     * @param firstServer rango desde donde empieza a buscar
     * @param lastServer rango hasta donde debe buscar
     * @param skds listas
     */
    public MyThread(String ipAddress,int firstServer, int lastServer, HostBlacklistsDataSourceFacade skds) {
        this.ipAddress = ipAddress;
        this.skds = skds;
        this.firstServer = firstServer;
        this.lastServer = lastServer;
        ocurrencesCount = 0;
        checkedListCount = 0;
        blackListOcurrences = new LinkedList<>();
    }

    /**
     * Metodo overraid run
     */
    public void run(){
        for(int i=firstServer; i<lastServer && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            if(skds.isInBlackListServer(i, ipAddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            } else {
                checkedListCount++;
            }
        }
    }

    /**
     * Metodo get que retorna la blackListOcurrences
     * @return blackListOcurrences
     */
    public LinkedList<Integer> getServers(){
        return blackListOcurrences;
    }

    /**
     * Metodo get que retorna la ocurrencesCount
     * @return ocurrencesCount
     */
    public int getOcurrencesCount(){
        return ocurrencesCount;
    }

    /**
     * Metodo get que retorna la checkedListCount
     * @return checkedListCount
     */
    public int getCheckedListCount() {
        return checkedListCount;
    }
}

