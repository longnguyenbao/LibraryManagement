/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nbl.librarymanagementapp;

import com.nbl.pojo.User;
import com.nbl.pojo.Role;
import com.nbl.services.UserServices;
import com.nbl.services.RoleServices;
import com.nbl.utils.Utils;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class SignInController implements Initializable {
    @FXML
    private ComboBox<Role> cbxRole;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loadComboBoxRole();
        this.cbxRole.getSelectionModel().select(2);
    }    
    
    public void signInHandler(ActionEvent evt) throws IOException, SQLException {
        if (cbxRole.getValue() == null) {
            Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
            return;
        }

        if (txtUsername.getText().isEmpty()) {
            Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
            return;
        }

        if (txtPassword.getText().isEmpty()) {
            Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        User user = new User();
        user.setUsername(txtUsername.getText());
        user.setPassword(txtPassword.getText());
        user.setRoleId(cbxRole.getSelectionModel().getSelectedItem().getId());

        UserServices s = new UserServices();
        User newUser = s.signIn(user);

        if(newUser != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLMenu.fxml"));
            Parent root = fxmlLoader.load();

            MenuController sceneMenu = fxmlLoader.getController();
            sceneMenu.setUser(newUser);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Menu");
            stage.show();
        }

        else {
            Utils.getBox("Sign in infomations are wrong!", Alert.AlertType.WARNING).show();
        }
    }
    
    public void signUpHandler(ActionEvent evt) throws IOException, SQLException {
        Utils.loadFXML("FXMLSignUp.fxml", "Sign Up");
    }
    
    private void loadComboBoxRole() {
        RoleServices s = new RoleServices();
        try {
            this.cbxRole.setItems(FXCollections.observableArrayList(s.getRoleList()));
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
