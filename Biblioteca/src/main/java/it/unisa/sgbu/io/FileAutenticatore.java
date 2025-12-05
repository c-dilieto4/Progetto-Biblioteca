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
public class FileAutenticatore implements IAutenticatore{
    private final String credentialsFile;
    private final IArchivioDati archivioDati;
    
    public FileAutenticatore(String credentialsFile, IArchivioDati archivioDati){
    }
    
    @Override
    public boolean verificaCredenziali(String user, String pass){
    }
}
