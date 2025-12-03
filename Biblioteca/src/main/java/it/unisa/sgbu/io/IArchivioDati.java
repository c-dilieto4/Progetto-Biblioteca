/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

/**
 *
 * @author dilie
 */
public interface IArchivioDati {
    
    public boolean salvaStato(Object dati, String nomeFile){
    }
    
    public Object caricaStato(String nomeFile){
    }
    
    public boolean verificaEsistenzaFile(String nomeFile){
    }
}
