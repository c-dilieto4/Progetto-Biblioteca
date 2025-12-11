/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.app;

import it.unisa.sgbu.domain.ValidatoreDati;
import it.unisa.sgbu.gui.GUIController;
import it.unisa.sgbu.gui.GUIView;
import it.unisa.sgbu.io.AuditTrail;
import it.unisa.sgbu.io.FileArchivio;
import it.unisa.sgbu.io.FileAutenticatore;
import it.unisa.sgbu.io.IArchivioDati;
import it.unisa.sgbu.io.IAutenticatore;
import it.unisa.sgbu.io.ILogger;
import it.unisa.sgbu.service.Anagrafica;
import it.unisa.sgbu.service.Catalogo;
import it.unisa.sgbu.service.RegistroPrestiti;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @brief Punto di ingresso (Bootstrap) dell'applicazione SGBU.
 * * Questa classe è responsabile dell'inizializzazione dell'intero sistema.
 * Configura l'ambiente, istanzia tutti i componenti architetturali (Model, View, Controller, I/O)
 * iniettando le dipendenze necessarie, e avvia il ciclo di vita dell'applicazione JavaFX.
 */
public class Main extends Application {

    private GUIController controller;
    
    // Costanti di configurazione
    private static final String PATH_DATI = "dati"; // Cartella salvataggio
    private static final String FILE_CREDENZIALI = "credenziali.dat"; // File login

    /**
     * @brief Metodo di avvio standard di JavaFX.
     * * Qui avviene la "Dependency Injection" manuale:
     * 1. Vengono creati i servizi di basso livello (I/O).
     * 2. Vengono creati i servizi di business (Model).
     * 3. Viene creato il Controller, iniettando i servizi.
     * 4. Viene creata la View, collegandola al Controller.
     * 5. Viene avviato il sistema (caricamento dati).
     * * @param primaryStage Lo stage primario fornito dalla piattaforma JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Creo l'archivio (passo null come logger temporaneamente per evitare cicli,
            // oppure FileArchivio userà System.err se il logger è null)
            IArchivioDati archivio = new FileArchivio(PATH_DATI, null);
            
            // Creo il Logger (Audit Trail)
            ILogger logger = new AuditTrail(null, archivio);
            
            // Creo l'Autenticatore
            IAutenticatore autenticatore = new FileAutenticatore(FILE_CREDENZIALI, archivio);
            
            ValidatoreDati validatore = new ValidatoreDati();
            Catalogo catalogo = new Catalogo();
            Anagrafica anagrafica = new Anagrafica();
            
            // Il Registro Prestiti ha bisogno di accedere a Catalogo e Anagrafica
            RegistroPrestiti registro = new RegistroPrestiti(catalogo, anagrafica);
            
            controller = new GUIController(
                    archivio, 
                    logger, 
                    autenticatore, 
                    catalogo, 
                    anagrafica, 
                    registro, 
                    validatore
            );
            
            controller.avviaSistema();
            
            GUIView view = new GUIView(controller, primaryStage);
            
            view.mostraFinestraLogin();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore critico durante l'inizializzazione dell'applicazione.");
        }
    }

    /**
     * @brief Metodo chiamato alla chiusura dell'applicazione.
     * * Garantisce che i dati vengano salvati correttamente quando l'utente chiude la finestra.
     * Implementa il requisito di persistenza alla chiusura.
     */
    @Override
    public void stop() {
        if (controller != null) {
            System.out.println("Chiusura applicazione rilevata. Salvataggio dati in corso...");
            controller.chiudiSistema();
        }
    }

    /**
     * @brief Entry point standard per le applicazioni Java.
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}