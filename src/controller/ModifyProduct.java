package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class creates the Modify Product controller.
 * @author Jackson Congdon
 */
public class ModifyProduct implements Initializable {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product testProduct;

    Stage stage;
    Parent scene;

    @FXML private TextField modifyproductMaxtxt;
    @FXML private TextField modifyproductMintxt;
    @FXML private TextField modifyproductNametxt;
    @FXML private TextField modifyproductInvtxt;
    @FXML private TextField modifyproductPricetxt;
    @FXML private Label modifyproductIDlabel;
    @FXML private TextField modifyproductaddSearchtxt;
    @FXML private TableView<Part> modifyproductaddTableView;
    @FXML private TableColumn<Part, Integer> modifyproductaddPartIDCol;
    @FXML private TableColumn<Part, String> modifyproductaddPartNameCol;
    @FXML private TableColumn<Part, Integer> modifyproductaddInventoryCol;
    @FXML private TableColumn<Part, Double> modifyproductaddPriceCol;
    @FXML private TableView<Part> modifyproductdeleteTableView;
    @FXML private TableColumn<Part, Integer> modifyproductdeletePartIDCol;
    @FXML private TableColumn<Part, String> modifyproductdeletePartNameCol;
    @FXML private TableColumn<Part, Integer> modifyproductdeleteInventoryCol;
    @FXML private TableColumn<Part, Double> modifyproductdeletePriceCol;

    /**
     * This is the method to add an associated part to the product being modified when the user clicks the Add button. It takes a part from the inventory in the top table and copies the part to the associated parts table below.
     * @param event user clicks the Add button
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = modifyproductaddTableView.getSelectionModel().getSelectedItem();
        testProduct.addAssociatedPart(selectedPart);
        testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());
    }

    /**
     * This is the method to search the parts inventory when the user clicks the Search button. It searches both exact part IDs and full or partial part names.
     * @param event user clicks the Search button
     */
    @FXML
    void onActionSearch(ActionEvent event) {
        String searchField = modifyproductaddSearchtxt.getText();
        try {
            ObservableList<Part> tempParts = Inventory.lookupPart(searchField);
            if (tempParts.size() == 0) {
                int searchID = Integer.parseInt(searchField);
                Part searchP = Inventory.lookupPart(searchID);
                tempParts.add(searchP);
                if (searchP == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No parts found by ID");
                    alert.showAndWait();
                }
            }
            modifyproductaddTableView.setItems(tempParts);
        }
        catch (NumberFormatException e) {
            modifyproductaddTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No parts found by name");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to refresh the top parts table with all parts in the inventory when the user deletes all text from the search field.
     * @param event user deletes all text from search field
     */
    @FXML
    void modifyProductSearchOnKeyTyped(KeyEvent event) {
        if (modifyproductaddSearchtxt.getText().isEmpty()) {
            modifyproductaddTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This is the method to remove an associated part from the product being modified when the user clicks the Remove Associated Part button. The associated part is removed from the bottom associated parts table, but the part remains in the parts inventory.
     * @param event users clicks the Remove Associated Part button
     */
    @FXML
    void onActionDelete(ActionEvent event) {
        Part delPart = modifyproductdeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            testProduct.deleteAssociatedParts(delPart);
            testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());
        }
    }

    /**
     * This is the method to modify an existing product when the user clicks the Save button. This method updates an existing product using the information inputted on the Modify Product screen and saves the modified product to the inventory. The method takes the existing product being modified and replaces that existing product within the inventory using the product ID.
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            testProduct.setId(Integer.parseInt(modifyproductIDlabel.getText()));
            testProduct.setName(modifyproductNametxt.getText());
            testProduct.setPrice(Double.parseDouble(modifyproductPricetxt.getText()));
            testProduct.setStock(Integer.parseInt(modifyproductInvtxt.getText()));
            testProduct.setMin(Integer.parseInt(modifyproductMintxt.getText()));
            testProduct.setMax(Integer.parseInt(modifyproductMaxtxt.getText()));
            testProduct.setAssociatedParts(testProduct.getAllAssociatedParts());

            if (Integer.parseInt(modifyproductMintxt.getText()) > Integer.parseInt(modifyproductMaxtxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The minimum must be less than or equal to the maximum");
                alert.showAndWait();
            } else if (Integer.parseInt(modifyproductInvtxt.getText()) > Integer.parseInt(modifyproductMaxtxt.getText()) || Integer.parseInt(modifyproductInvtxt.getText()) < Integer.parseInt(modifyproductMintxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The stock value must be within the minimum and maximum range");
                alert.showAndWait();
            } else if (modifyproductNametxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The name must not be empty");
                alert.showAndWait();
            }
            else {
                Inventory.updateProduct(testProduct.getId(), testProduct);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all changes, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method takes the product selected on the Main Screen to be modified and sends the product data to the fields on the Modify Product screen.
     * @param product the product selected on the Main Screen to be modified
     */
    public void sendProduct(Product product) {
        testProduct = product;

        modifyproductIDlabel.setText(String.valueOf(testProduct.getId()));
        modifyproductNametxt.setText(testProduct.getName());
        modifyproductPricetxt.setText(String.valueOf(testProduct.getPrice()));
        modifyproductInvtxt.setText(String.valueOf(testProduct.getStock()));
        modifyproductMaxtxt.setText(String.valueOf(testProduct.getMax()));
        modifyproductMintxt.setText(String.valueOf(testProduct.getMin()));
        modifyproductdeleteTableView.setItems(testProduct.getAllAssociatedParts());
    }

    /**
     * This is the method to set the top all parts table view and the bottom associated parts table view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Top table view
        modifyproductaddTableView.setItems(Inventory.getAllParts());
        modifyproductaddPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyproductaddPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyproductaddInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyproductaddPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Bottom table view
        modifyproductdeletePartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyproductdeletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyproductdeleteInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyproductdeletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}