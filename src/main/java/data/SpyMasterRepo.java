package data;

import domain.Message;

import java.util.List;

public interface SpyMasterRepo {
    List<Message> getMessages(boolean isSender, String username);

    void updateContent(String content, Message message);

    void deleteMessage(Message message);
}
