/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.app;

import it.unisa.sgbu.domain.Credenziali;
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
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @brief Punto di ingresso dell'applicazione SGBU.
 */
public class Main extends Application {

    private GUIController controller;
    
    // Costanti di configurazione
    private static final String PATH_DATI = "dati"; // Cartella salvataggio
    private static final String FILE_CREDENZIALI = "credenziali.dat"; // File login

    @Override
    public void start(Stage primaryStage) {
        try {
            // Creo l'archivio 
            IArchivioDati archivio = new FileArchivio(PATH_DATI, null);
            
            // Se il file credenziali non esiste, ne creo uno con dei dati
            if (!archivio.verificaEsistenzaFile(FILE_CREDENZIALI)) {
                Credenziali mieCredenziali = new Credenziali("admin", "password");
                archivio.salvaStato(mieCredenziali, FILE_CREDENZIALI);
                System.out.println("Credenziali create: admin / password");
            }
            
            // Creo l'Autenticatore
            IAutenticatore autenticatore = new FileAutenticatore(FILE_CREDENZIALI, archivio);
            
            ValidatoreDati validatore = new ValidatoreDati();
            Catalogo catalogo = new Catalogo();
            Anagrafica anagrafica = new Anagrafica();
            
            // Il Registro Prestiti ha bisogno di accedere a Catalogo e Anagrafica
            RegistroPrestiti registro = new RegistroPrestiti(catalogo, anagrafica);
            
            // Creo il Logger (Audit Trail)
            List<String> logs = (List<String>)archivio.caricaStato(AuditTrail.NOME_FILE_LOG);
            ILogger logger = new AuditTrail(logs, archivio);           
            
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

    @Override
    public void stop() {
        if (controller != null) {
            System.out.println("Chiusura applicazione rilevata. Salvataggio dati in corso...");
            controller.chiudiSistema();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}