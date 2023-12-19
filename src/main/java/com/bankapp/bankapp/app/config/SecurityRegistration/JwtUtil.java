package com.bankapp.bankapp.app.config.SecurityRegistration;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class JwtUtil {

    public static UUID encode(String name) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("nam");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return UUID.nameUUIDFromBytes(md.digest(name.getBytes()));
    }

}