package com.ominext.trainning.pharmacy.utils.constant;

public class Validate {
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
        + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
