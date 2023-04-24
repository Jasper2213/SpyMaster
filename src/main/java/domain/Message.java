package domain;

import java.nio.charset.StandardCharsets;

public class Message {

    private static final String LOCKED = "\uD83D\uDD12";
    private static final String UNLOCKED = "  ";

    private EncodedState encodedState;

    private String sender;
    private String receiver;
    private String content;

    public Message(Message other) {
        this(
                other.sender,
                other.receiver,
                other.encodedState,
                other.content
        );
    }

    public Message(String sender, String receiver, String content) {
        this(
                sender,
                receiver,
                EncodedState.PLAIN_TEXT,
                content
        );
    }

    public Message(String sender, String receiver, EncodedState encodedState, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;

        this.encodedState = encodedState;
    }

    public boolean isPlainText() {
        return encodedState == EncodedState.PLAIN_TEXT;
    }

    @Override
    public String toString() {
        if (isPlainText()) {
            return String.format("%s (%s to %s)", content, sender, receiver);
        } else {
            return String.format("%s (%s) to %s (%s)", sender, isSenderEncoded()?LOCKED:UNLOCKED, receiver, isReceiverEncoded()?LOCKED:UNLOCKED );
        }
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public boolean isSenderEncoded() {
        return encodedState.isSenderEncoded();
    }

    public boolean isReceiverEncoded() {
        return encodedState.isReceiverEncoded();
    }

    public EncodedState getEncodedState() {
        return encodedState;
    }
    public void setEncodedState(EncodedState encodedState) {
        this.encodedState = encodedState;
    }
}
