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

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrore;

    private GUIController sistema;
    private GUIView mainView;

    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
    }

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