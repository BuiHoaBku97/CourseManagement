package org.example.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String hash(String rawPassword) {
        String normalizedPassword = normalize(rawPassword);
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        byte[] hash = deriveKey(normalizedPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        return formatHash(ITERATIONS, salt, hash);
    }

    public static boolean matches(String rawPassword, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        try {
            String[] parts = storedHash.split("\\$");
            if (parts.length != 4 || !"PBKDF2".equals(parts[0])) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash =
                    deriveKey(
                            normalize(rawPassword).toCharArray(),
                            salt,
                            iterations,
                            expectedHash.length * 8);
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static String formatHash(int iterations, byte[] salt, byte[] hash) {
        return "PBKDF2$"
                + iterations
                + "$"
                + Base64.getEncoder().encodeToString(salt)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    private static byte[] deriveKey(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            return keyFactory.generateSecret(spec).getEncoded();
        } catch (Exception exception) {
            throw new IllegalStateException("Khong the ma hoa mat khau.", exception);
        }
    }

    private static String normalize(String value) {
        return Objects.requireNonNull(value, "password").trim();
    }
}
