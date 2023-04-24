package domain;

public enum EncodedState {

    PLAIN_TEXT(false, false),
    SENDER_ENCODED(true, false),
    FULL_ENCODED(true, true),
    RECEIVER_ENCODED(false, true);

    private final boolean senderEncoded;
    private final boolean receiverEncoded;

    EncodedState(boolean senderEncoded, boolean receiverEncoded) {
        this.senderEncoded = senderEncoded;
        this.receiverEncoded = receiverEncoded;
    }

    public boolean isSenderEncoded() {
        return senderEncoded;
    }

    public boolean isReceiverEncoded() {
        return receiverEncoded;
    }

    public EncodedState prev() {
        return values()[(values().length + ordinal() - 1)%values().length];
    }

    public EncodedState next() {
        return values()[(ordinal()+1)%values().length];
    }

}
