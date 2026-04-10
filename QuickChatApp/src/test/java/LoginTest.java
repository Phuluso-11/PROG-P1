package com.mycompany.quickchatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    Login login = new Login();

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertEquals("Welcome John Doe it is great to see you.", login.registerUser("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", login.registerUser("John", "Doe", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testPasswordCorrectlyFormatted() {
        assertEquals("Welcome John Doe it is great to see you.", login.registerUser("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testPasswordIncorrectlyFormatted() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", login.registerUser("John", "Doe", "kyl_1", "password", "+27838968976"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.", login.checkCellPhoneNumber("+27838968976") ? "Cell number successfully captured." : "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.");
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        assertEquals("Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.", login.checkCellPhoneNumber("08966553") ? "Cell number successfully captured." : "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.");
    }

    @Test
    public void testLoginSuccessful() {
        login.registerUser("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        login.registerUser("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrongUser", "wrongPass"));
    }

    @Test
    public void testUsernameCheckTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameCheckFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordCheckTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordCheckFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCheckTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneCheckFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}