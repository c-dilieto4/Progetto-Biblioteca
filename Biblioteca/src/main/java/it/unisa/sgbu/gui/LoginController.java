/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * @brief Controller per la schermata di Login.
 * 
 * Gestisce l'autenticazione dell'operatore bibliotecario al sistema.
 * Intercetta l'input utente, invoca la logica di verifica credenziali e 
 * controlla la transizione verso la Dashboard principale in caso di successo.
 */
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrore;

    private GUIController sistema;
    private GUIView mainView;

    
    /**
     * @brief Inizializza il controller con i riferimenti al sistema e alla vista principale.
     * 
     * @param[in] sistema Istanza del controller logico (Facade) per la verifica delle credenziali.
     * @param[in] view Istanza del gestore delle viste per effettuare il cambio scena.
     * 
     * @pre 
     * - I parametri sistema e view non devono essere null.
     * 
     * @post 
     * - Il controller è collegato alla logica di business e pronto a gestire eventi.
     */
    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
    }

    
    /**
     * @brief Gestisce l'evento di tentativo di login (click sul pulsante "Accedi").
     * 
     * Recupera username e password dai campi di testo, verifica che non siano vuoti
     * e delega al sistema la validazione delle credenziali.
     * 
     * @pre 
     * - I campi di testo txtUsername e txtPassword devono essere inizializzati.
     * 
     * @post 
     * - Se i campi sono vuoti: viene mostrato un messaggio di errore ("Inserisci username e password").
     * - Se il login ha successo: viene invocato mainView.avviaInterfaccia() per mostrare la Dashboard.
     * - Se il login fallisce: viene mostrato un messaggio di errore e il campo password viene pulito.
     */
    @FXML
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblErrore.setText("Inserisci username e password.");
            lblErrore.setVisible(true);
            return;
        }

        if (sistema.gestisciLogin(user, pass)) {
            mainView.avviaInterfaccia(); 
        } else {
            lblErrore.setText(MessaggiInterfaccia.CREDENZIALI_NON_VALIDE);
            lblErrore.setVisible(true);
            txtPassword.clear();
        }
    }
}