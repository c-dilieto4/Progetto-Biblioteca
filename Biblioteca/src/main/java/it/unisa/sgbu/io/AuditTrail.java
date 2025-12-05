/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

import java.util.*;

/**
 *
 * @author dilie
 */
public class AuditTrail implements ILogger{
    
    private List<String> logRecords;
    private IArchivioDati archivio;

    public AuditTrail(List<String> logRecords, IArchivioDati archivio) {
    }
    
    @Override
    public void registraAzione(String azione){
    }
    
    @Override
    public List<String> caricaLog(){
    }
    
    @Override
    public void salvaLog(){
    }
    
    @Override
    public List<String> visualizzaLog(){
    }
    
}
