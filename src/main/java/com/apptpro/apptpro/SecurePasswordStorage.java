package com.apptpro.apptpro;
import com.apptpro.apptpro.DAO.UserDAO;
import com.apptpro.apptpro.Models.User;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SecurePasswordStorage {

    private final UserDAO userDAO = new UserDAO();



    public User authenticateUser(String inputUser, String inputPass) throws Exception {
        //Init database instance
        User user = userDAO.getUser(inputUser);
        if (user != null) {
            String salt = user.getUserSalt();
            String calculatedHash = getEncryptedPassword(inputPass, salt);
            if (calculatedHash.equals(user.getUserSaltedPassword())){
                return user;
            } else{
                return null;
            }
        }
        else {
            return null;
        }
    }

    public boolean signUp(String userName, String password,String firstName, String lastName) throws Exception {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);

        User newUser = new User(userName,encryptedPassword,firstName,lastName,salt);
        return saveUser(newUser);
    }

    // Get a encrypted password using PBKDF2 hash algorithm
    private String getEncryptedPassword(String password, String salt) throws Exception {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 20000; // NIST specifies 10000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        byte[] encBytes = f.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encBytes);
    }

    // Returns base64 encoded salt
    private String getNewSalt() throws Exception {
        // Don't use Random!
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // NIST recommends minimum 4 bytes. We use 8.
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private boolean saveUser(User user) {
        try {
            userDAO.saveUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}



