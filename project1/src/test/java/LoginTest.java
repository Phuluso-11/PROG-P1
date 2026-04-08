package com.mycompany.project1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome John Doe it is great to see you.", login.registerUser());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", login.registerUser());
    }

    @Test
    public void testPasswordCorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome John Doe it is great to see you.", login.registerUser());
    }

    @Test
    public void testPasswordIncorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", login.registerUser());
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Cell number successfully captured.", login.checkCellPhoneNumber() ? "Cell number successfully captured." : "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.");
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.", login.checkCellPhoneNumber() ? "Cell number successfully captured." : "Cell number incorrectly formatted or does not contain an international code; please correct the number and try again.");
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrongUser", "wrongPass"));
    }

    @Test
    public void testUsernameCheckTrue() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameCheckFalse() {
        Login login = new Login("John", "Doe", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordCheckTrue() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordCheckFalse() {
        Login login = new Login("John", "Doe", "kyl_1", "password", "+27838968976");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCheckTrue() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneCheckFalse() {
        Login login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }
}