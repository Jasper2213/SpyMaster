package util;

import org.jasypt.util.text.StrongTextEncryptor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class Crypto {

    private static final Crypto INSTANCE = new Crypto();
    private static final String KEY = "SECRET";
    private final StrongTextEncryptor encryptor;

    public static Crypto getInstance() {
        return INSTANCE;
    }

    private Crypto() {
        encryptor = new StrongTextEncryptor();
        encryptor.setPassword(KEY);
    }

    public String encrypt(String in) {
        return encryptor.encrypt(in);
    }

    public String decrypt(String in) {
        return encryptor.decrypt(in);
    }

    public String hash(String in) {
        return BCrypt.hashpw(in, BCrypt.gensalt());
    }

    public boolean comparePasswords(String in, String hashed) {
        try {
            return BCrypt.checkpw(in, hashed);
        }
        catch (IllegalArgumentException ex) {
            throw new SpyMasterException("Failed to check password");
        }
    }

    // database credentials encrypten, paswoord van gebruikers hashen

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("What do you want to encrypt?"); // NOSONAR: this IS a CL-application.
        String line = in.nextLine();
        System.out.println(Crypto.getInstance().encrypt(line));  // NOSONAR: this IS a CL-application.
    }

}
