package ui.fx;

import domain.Message;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import service.SpyMasterService;
import util.SpyMasterException;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverviewController {

    private static final Logger LOGGER = Logger.getLogger(OverviewController.class.getName());

    @FXML
    private ComboBox<String> cbxFind;
    @FXML
    private TextField txtUsername;
    @FXML
    private ListView<Message> lvMessages;

    private SpyMasterService service;

    public OverviewController() {
        service = new SpyMasterService();
    }

    @FXML
    public void initialize() {
        cbxFind.getItems().setAll(
                "Sender",
                "Receiver"
        );

        lvMessages.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                service.setSelectedMessage(lvMessages.getSelectionModel().getSelectedItem());
                showMessageDetails();
            }
        });
    }

    private void showMessageDetails() {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("i22n");
            FXMLLoader loader = new FXMLLoader(StartUp.class.getResource("/fxml/MessageDetails.fxml"), resourceBundle);
            Parent parent = loader.load();
            DetailsController controller = loader.getController();
            controller.setService(service);
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(parent));
            secondStage.show();
        }
        catch (IOException ex) {
            LOGGER.log(Level.INFO, "Failed to show second screen", ex);
            showError("Failed to show second screen");
        }
    }

    public void find(ActionEvent actionEvent) {
        try {
            boolean isSender = cbxFind.getSelectionModel().getSelectedItem().equals("Sender");
            String username = txtUsername.getText();

            lvMessages.setItems(FXCollections.observableArrayList(
                    service.getMessages(isSender, username)
            ));
        }
        catch (SpyMasterException ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
        error.showAndWait();
    }
}
