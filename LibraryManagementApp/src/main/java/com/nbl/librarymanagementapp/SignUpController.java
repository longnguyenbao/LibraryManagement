/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nbl.librarymanagementapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.nbl.services.UserServices;
import com.nbl.utils.Utils;
import com.nbl.pojo.User;
import com.nbl.pojo.Role;
import com.nbl.pojo.Department;
import com.nbl.services.RoleServices;
import com.nbl.services.DepartmentServices;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class SignUpController implements Initializable {
    @FXML
    private ComboBox<Role> cbxRole;
    @FXML
    private ComboBox<Department> cbxDepartment;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private RadioButton rdoMale;
    @FXML
    private RadioButton rdoFemale;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtAddress;    
    @FXML
    private TextField txtPhone;
    @FXML
    private PasswordField txtRetypePassword;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rdoMale.setSelected(true);
        
        this.loadComboBoxRole();
        this.cbxRole.getSelectionModel().select(0);
        
        this.loadComboBoxDepartment();
        this.cbxDepartment.getSelectionModel().select(0);
        
        this.txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }    
    
    public void signUpHandler(ActionEvent evt) throws IOException, SQLException {
        if (txtUsername.getText().isEmpty()) {
            Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (cbxRole.getValue() == null) {
            Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if(cbxRole.getSelectionModel().getSelectedItem().getId() == 2){
            Utils.getBox("You can not sign up with this role!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (cbxDepartment.getValue() == null) {
            Utils.getBox("Department is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (txtPassword.getText().isEmpty()) {
            Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (!txtPassword.getText().equals(txtRetypePassword.getText()) ) {
            Utils.getBox("Password does not match!", Alert.AlertType.WARNING).show();
            return;
        }
        
        User user = new User();
        user.setUsername(txtUsername.getText());
        user.setName(txtName.getText());
        user.setEmail(txtEmail.getText());
        user.setGender(pickGender());
        user.setRoleId(cbxRole.getSelectionModel().getSelectedItem().getId());
        user.setDepartmentId(cbxDepartment.getSelectionModel().getSelectedItem().getId());
        user.setDateOfBirth(Utils.parseDate(dpDateOfBirth.valueProperty().get()));
        user.setRegistrationDate(Utils.parseDateNow());
        user.setExpirationDate(Utils.parseDateNow());
        user.setAddress(txtAddress.getText());
        user.setPhone(txtPhone.getText());
        user.setPassword(txtPassword.getText());
   
        UserServices s = new UserServices();
        if(s.signUp(user)) {
            Utils.getBox("Sign up success!", Alert.AlertType.INFORMATION).show();
        }
        else {
            Utils.getBox("This username is already registered!", Alert.AlertType.ERROR).show();
        }
    }
    
    private String pickGender(){
        if(rdoMale.isSelected()){
            return rdoMale.getText();
        }
        else {
            return rdoFemale.getText();
        }       
    }
    
    private void loadComboBoxRole() {
        RoleServices s = new RoleServices();
        try {
            this.cbxRole.setItems(FXCollections.observableArrayList(s.getRoleList()));
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadComboBoxDepartment() {
        DepartmentServices s = new DepartmentServices();
        try {
            this.cbxDepartment.setItems(FXCollections.observableArrayList(s.getDepartmentList()));
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
