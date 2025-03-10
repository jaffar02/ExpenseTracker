package com.example.AuthService.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoInputValidation {

    public static Boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * At least one uppercase letter.
     * At least one lowercase letter.
     * At least one digit.
     * At least one special character.
     * Minimum 8 characters.
     * @param password
     * @return isValid [Boolean]
     */
    public static Boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
