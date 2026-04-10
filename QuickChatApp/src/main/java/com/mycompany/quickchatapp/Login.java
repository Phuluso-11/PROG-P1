package com.mycompany.quickchatapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    String username;
    String password;
    String cellPhoneNumber;
    String firstName;
    String lastName;

    public Login() {
    }

    //method to validate the username
    boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    //method to validate the password complexity
    boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasNumber = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasNumber && hasSpecial;
    }

    //method to validate the cellphone number
    boolean checkCellPhoneNumber(String cellphonenumber) {
        String regex = "^(\\+27|0)[6-8][0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellphonenumber);
        return matcher.matches();
    }

    //method to return registration status
    String registerUser(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        return "Welcome " + firstName + " " + lastName + " it is great to see you.";
    }

    //method to verify login details
    boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    //method to return login status
    String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}