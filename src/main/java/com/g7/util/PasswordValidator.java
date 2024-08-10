package com.g7.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    /**
     * Here are rules for password:
     *
     * Must have at least one uppercase character
     * Must have at least one lowercase character
     * Must have at least one numeric character
     * Must have at least one special symbol among @$.!-+
     * Password length should be between 8 and 20
     */

    // fullRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$.!-+]).{8,20}$"
    private static final String uppercaseRegex = ".*[A-Z].*";
    private static final String lowercaseRegex = ".*[a-z].*";
    private static final String numericRegex = ".*\\d.*";
    private static final String symbolRegex = ".*[@$.!-+].*";
    private static final String lengthRegex = ".{8,20}";


    /*
    Return nulls if password is valid
     */
    public boolean isValid(String password) {
        if(!Pattern.compile(uppercaseRegex).matcher(password).matches())
            return false;

        if(!Pattern.compile(lowercaseRegex).matcher(password).matches())
            return false;

        if(!Pattern.compile(numericRegex).matcher(password).matches())
            return false;

        if(!Pattern.compile(symbolRegex).matcher(password).matches())
            return false;

        if(!Pattern.compile(lengthRegex).matcher(password).matches())
            return false;
        return true;
    }

}