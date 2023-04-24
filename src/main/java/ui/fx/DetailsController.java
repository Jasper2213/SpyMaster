package ui.fx;

import domain.Message;
import domain.XOREncoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.SpyMasterService;

public class DetailsController {

    @FXML
    private TextField txtSender;
    @FXML
    private TextField txtReceiver;
    @FXML
    private TextArea txtContent;

    @FXML
    private Button btnAddSenderPassword;
    @FXML
    private Button btnAddReceiverPassword;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private SpyMasterService service;

    public void setService(SpyMasterService service) {
        this.service = service;
    }

    public void showDetails(ActionEvent actionEvent) {
        Message selectedMessage = service.getSelectedMessage();

        txtSender.setText(selectedMessage.getSender());
        txtReceiver.setText(selectedMessage.getReceiver());
        txtContent.setText(selectedMessage.getContent());

        if (selectedMessage.isSenderEncoded() && selectedMessage.isReceiverEncoded()) {
            btnAddSenderPassword.setVisible(true);
            btnAddReceiverPassword.setVisible(false);
        }
        else if (selectedMessage.isSenderEncoded() || selectedMessage.isReceiverEncoded()) {
            btnAddSenderPassword.setVisible(false);
            btnAddReceiverPassword.setVisible(true);
        }
        else if (selectedMessage.isPlainText()) {
            btnAddSenderPassword.setVisible(false);
            btnAddReceiverPassword.setVisible(false);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(true);
        }
    }

    public void addSenderPassword(ActionEvent actionEvent) {
        String password = showPasswordPrompt(txtSender.getText());
        setNewContent(password);
    }

    public void addReceiverPassword(ActionEvent actionEvent) {
        String password = showPasswordPrompt(txtReceiver.getText());
        setNewContent(password);
    }

    private String showPasswordPrompt(String username) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Password Input");
        inputDialog.setHeaderText("Enter your password, " + username);
        inputDialog.showAndWait();

        return inputDialog.getEditor().getText();
    }

    private void setNewContent(String password) {
        XOREncoder encoder = new XOREncoder(password);
        String newContent = encoder.apply(txtContent.getText());
        txtContent.setText(newContent);

        Message selectedMessage = service.getSelectedMessage();
        selectedMessage.setEncodedState(selectedMessage.getEncodedState().next());

        btnUpdate.setDisable(false);
    }

    public void updateContent(ActionEvent actionEvent) {
        String content = txtContent.getText();

        service.updateContent(content, service.getSelectedMessage());
    }

    public void deleteMessage(ActionEvent actionEvent) {
        service.deleteMessage(service.getSelectedMessage());
    }
}
