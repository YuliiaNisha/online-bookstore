package project.bookstore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import project.bookstore.exception.PasswordHashingException;

public class HashUtil {

    public static final String ALGORITHM = "SHA-512";
    public static final int SALT_LENGTH = 16;
    public static final String STRING_FORMAT = "%02x";

    public static byte[] getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(salt);
            byte[] digestedPassword = messageDigest.digest(password.getBytes());
            for (byte b : digestedPassword) {
                hashedPassword.append(String.format(STRING_FORMAT, b));
            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("Can't hash password.", e);
        }
    }
}
