package service;

import data.Repositories;
import data.SpyMasterRepo;
import domain.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.fx.DetailsController;
import ui.fx.StartUp;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class SpyMasterService {

    private final SpyMasterRepo repo = Repositories.getSpyMasterRepo();

    private Message selectedMessage;

    public Message getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(Message message) {
        this.selectedMessage = message;
    }

    public List<Message> getMessages(boolean isSender, String username) {
        return repo.getMessages(isSender, username);
    }

    public void updateContent(String content, Message message) {
        repo.updateContent(content, message);
    }

    public void deleteMessage(Message message) {
        repo.deleteMessage(message);
    }
}
