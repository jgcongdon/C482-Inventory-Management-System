package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class creates the Add Part controller.
 * @author Jackson Congdon
 */
public class AddPart implements Initializable {

    private static int id = 200;

    Stage stage;
    Parent scene;

    @FXML private RadioButton addpartInhousebutton;
    @FXML private RadioButton addpartOutsourcedbutton;
    @FXML private TextField addpartMaxtxt;
    @FXML private TextField addpartMintxt;
    @FXML private TextField addpartNametxt;
    @FXML private TextField addpartInvtxt;
    @FXML private TextField addpartPricetxt;
    @FXML private TextField addpartCompanytxt;
    @FXML private Label addpartIDlabel;
    @FXML private Label MachineOrCompany;

    /**
     * <p><b>
     * RUNTIME ERROR: When creating the addPart controller, early drafts could not handle blank fields or incorrect number formats (integers in price, doubles in min or max). My first attempts involved individual validations of each field to attempt to stop the addPart process if an incorrect number was encountered. These attempts were wholly unsuccessful at preventing the NumberFormatException. After watching the exceptions webinar, meeting with a course instructor, and discussing with a classmate, I successfully added a try/catch argument where if an incorrect number format was entered into a field, then a WARNING alert was triggered to inform the user to input correct values into each field.
     * </b></p>
     * This is the method to save a new part when the user clicks the Save button. This method creates a new part using the information inputted on the Add Part screen and saves the new part to the inventory.
     * @param event user clicks the Save button
     * @throws IOException
     * @throws NumberFormatException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(addpartIDlabel.getText());
            String name = addpartNametxt.getText();
            int stock = Integer.parseInt(addpartInvtxt.getText());
            double price = Double.parseDouble(addpartPricetxt.getText());
            int min = Integer.parseInt(addpartMintxt.getText());
            int max = Integer.parseInt(addpartMaxtxt.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The minimum must be less than or equal to the maximum");
                alert.showAndWait();
            }
            else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The stock value must be within the minimum and maximum range");
                alert.showAndWait();
            }
            else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The name must not be empty");
                alert.showAndWait();
            }
            else {
                //In-House radio button is selected
                if (addpartInhousebutton.isSelected()) {
                    int machineID = Integer.parseInt(addpartCompanytxt.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                }
                //Outsourced radio button is selected
                else if (addpartOutsourcedbutton.isSelected()) {
                    String companyName = addpartCompanytxt.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }
                //Return to Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to return to the main screen when the user clicks the Cancel button. No inputted information is saved.
     * @param event user clicks the Cancel button
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Machine ID when the user clicks the In-House radio button.
     * @param event user clicks In-House radio button
     */
    @FXML
    void onActionInhouse(ActionEvent event){
        MachineOrCompany.setText("Machine ID");
        addpartOutsourcedbutton.setSelected(false);
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Company Name when the user clicks the Outsourced radio button.
     * @param event user clicks Outsourced radio button
     */
    @FXML
    void onActionOutsourced(ActionEvent event){
        MachineOrCompany.setText("Company Name");
        addpartInhousebutton.setSelected(false);
    }

    /**
     * This is the method to get the new part id beginning at 200.
     * @return the part id
     */
    public static int getPartIDCount() {
        id++;
        return id;
    }

    /**
     * This is the method to set the radio buttons to In-House by default, get the new part id, and set the new part id to the uneditable id field.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addpartInhousebutton.setSelected(true);
        id = getPartIDCount();
        addpartIDlabel.setText(String.valueOf(id));
    }
}