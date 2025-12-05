/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.domain.Libro;
import it.unisa.sgbu.domain.Prestito;
import it.unisa.sgbu.domain.Utente;
import java.util.List;

/**
 *
 * @author gerry
 */
public class GUIView {
    private final GUIController Sistema;
    private final MessaggiInterfaccia messaggi;
    
    public GUIView(GUIController Sistema){
    }
    
    public void avviaInterfaccia(){
    }
    
    public void gestisciAggiuntaLibro(){
    }
    
    public void gestisciAggiuntaUtente(){
    }
    
    public void gestisciModificaLibro(){
    }
    
    public void gestisciModificaUtente(){
    }
    
    public void gestisciEliminazioneUtente(){
    }
    
    public void gestisciEliminazioneLibro(){
    }
    
    public void gestisciRegistrazionePrestito(){
    }
    
    public void gestisciRestituzionePrestito(){
    }
    
    public void gestisciRicercaLibro(){
    }
    
    public void gestisciRicercaUtente(){
    }
    
    public void mostraListaLibri(List<Libro> Lista){
    }
    
    public void mostraListaUtenti(List<Libro> Lista){
    }
    
    public void mostraListaPrestiti(List<Prestito> Lista){
    }
    
    public void mostraReportUtente(Utente u){
    }
    
    public void mostraMessaggio(MessaggiInterfaccia msg){
    }
    
    public void mostraFinestraLogin(){
    }
    
    public void mostraLogDiSistema(List<String> log){
    }
    
    
}
