package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Inventory Management System (C482 - Software I)</h1>
 * This class creates an app that manages an inventory of parts and products.
 * <p><b>
 * FUTURE ENHANCEMENT: To extend functionality to the next version if I were to update the application, I would add a history feature for the various part and product fields. It could be very beneficial to know the long term trends of how much outsourced parts cost, the inventory levels of various parts and products, and a record to track deleted parts and products to reference later. Price history could alert management when costs are increasing and lead to researching alternative parts or suppliers. Inventory history could show different months when a certain part or product is needed more or less, and allow management to increase or decrease production or purchasing as appropriate. A record of deleted parts would give access to the company's past ordering and manufacturing trends, and see which deprecated parts or products may be useful again in the future.
 * </b></p>
 * RUNTIME ERROR can be found at onActionSave in the AddPart.java
 * After unzipping c482jacksoncongdon.zip, the Javadocs files can be found at /javadoc
 * @author Jackson Congdon
 */
public class Main extends Application {

    /**
     * Loads MainScreen.fxml to start the GUI and display the initial MainScreen.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    }

    /** This is the main method, which launches the database program.
     * @param args
     */
        public static void main(String[] args) {
            launch(args);
        }
}