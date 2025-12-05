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
public class FileArchivio implements IArchivioDati{
   
    private String pathDati;
    private ILogger logger;
    
    public FileArchivio(String pathDati, ILogger logger){
    }
    
    @Override
    public boolean salvaStato(Object dati, String nomeFile){
    }
 
    @Override
    public Object caricaStato(String nomeFile){
    }
    
    @Override
    public boolean verificaEsistenzaFile(String nomeFile){
    }
}
