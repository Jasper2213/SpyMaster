package domain;

import java.nio.charset.StandardCharsets;

public class XOREncoder implements Encoder {

    private final String password;

    public XOREncoder(String password) {
        this.password = password;
    }

    @Override
    public String apply(String content) {
        return xor(content, password);
    }

    private static String xor(String content, String password) {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

        byte[] resultBytes = new byte[contentBytes.length];
        for (int i = 0; i < contentBytes.length; i++) {
            resultBytes[i] = (byte) (contentBytes[i] ^ passwordBytes[i%passwordBytes.length]);
        }

        return new String(resultBytes, StandardCharsets.UTF_8);
    }
}
