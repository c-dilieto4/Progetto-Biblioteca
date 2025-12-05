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
    private ValidatoreDati valida;
    private IAutenticatore autenticatore;
    
    public GUIController(IArchivioDati archivio, ILogger logger, IAutenticatore autenticatore, Catalogo catalogo, Anagrafica anagrafica, RegistroPrestiti registro, ValidatoreDati valida){
    }
    
    public boolean avviaSistema(){
    }
    
    public boolean chiudiSistema(){
    }
    
    public boolean aggiungiUtente(Utente u){
    }
    
    public boolean aggiungiLibro(Libro l){
    }
    
    public boolean modificaUtente(String matrOriginale, Utente uNuovo){
    }
    
    public boolean modificaLibro(String isbnOriginale, Libro lNuovo){
    }
    
    public List<Libro> cercaLibro(String query, String campo){
    }
    
    public List<Utente> cercaUtente(String query, String campo){
    }
    
    public boolean rimuoviUtente(String matr){
    }
    
    public boolean rimuoviLibro(String isbn){
    }
    
    public boolean gestisciPrestito(String isbn, String matricola, LocalDate dataPrevistaRestituzione){
    }
    
    public boolean gestisciRestituzione(int idPrestito, LocalDate dataEffettivaRestituzione){
    }
    
    public List<Prestito> ottieniReportPrestiti(){
    }
        
    public List<Libro> ottieniCatalogoOrdinato(){
    }
    
    public List<Utente> ottieniAnagraficaOrdinata(){
    }
    
    public boolean gestisciLogin(String user, String pass){
    }

    
}
