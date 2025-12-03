/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.io.*;
import it.unisa.sgbu.service.*;
import it.unisa.sgbu.domain.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author dilie
 */
public class GUIController {
    
    private IArchivioDati archivio;
    private ILogger logger;
    private Catalogo catalogo;
    private Anagrafica anagrafica;
    private RegistroPrestiti registro;
    
    public GUIController(IArchivioDati archivio, ILogger logger){
    }
    
    public boolean avviaSistema(){
    }
    
    public boolean chiudiSistema(){
    }
    
    public boolean aggiugniUtente(Utente u){
    }
    
    public boolean aggiungiLibro(Libro l){
    }
    
    public boolean gestisciPrestito(String isbn, String matricola, LocalDate dataPrevista){
    }
    
    public boolean gestisciRestituzione(int idPrestito){
    }
    
    public List<Prestito> ottieniReportPrestiti(){
    }
        
    public List<Libro> ottieniCatalogoOrdinato(){
    }

    
}
