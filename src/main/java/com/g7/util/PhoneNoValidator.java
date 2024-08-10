package com.g7.util;

import org.springframework.stereotype.Component;

@Component
public class PhoneNoValidator {
    public boolean isValid(String phoneNumber) {
        // Regular expression pattern for Turkish phone numbers
        String regex = "^\\+90\\d{10}$";

        // Return true if the phone number matches the pattern, false otherwise
        return phoneNumber.matches(regex);
    }
}
