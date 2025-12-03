/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Libro;
import it.unisa.sgbu.io.*;
import java.util.*;

/**
 *
 * @author dilie
 */
public class Catalogo {
    
    private Map<String, Libro> collezione;
    private IArchivioDati archivio;
    private ILogger logger;
    
    public Catalogo(IArchivioDati archivio, ILogger logger){
    }
    
    public boolean aggiungiLibro(Libro l){
    }
    
    public boolean rimuoviLibro(String isbn){
    }
    
    public boolean modificaLibro(String isbn, Libro nl){
    }
    
    public Libro getLibro(String isbn){
    }
    
    public List<Libro> ricerca(String query, String campo){
    }
    
    public List<Libro> visualizzaOrdinata(){
    }
    
}
