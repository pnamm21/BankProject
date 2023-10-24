package com.bankapp.bankapp.app.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDValidator {
    public static boolean isValid(String uuid){

        Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        Matcher matcher = pattern.matcher(uuid);

        if (matcher.find()){
            return matcher.group().equals(uuid);
        }
        return false;
    }
}