// Part 2 unit tests
// Author: Phuluso Kone
// Date: 23 May 2026
// Purpose: Unit tests for Message class - Part 2
package com.mycompany.quickchatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    Message message = new Message();

    
    // Tests: Message length validation
    

    @Test
    public void testMessageLengthSuccess() {
        // Message within 250 characters should pass
        String testMessage = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("Message ready to send.", Message.validateMessageLength(testMessage));
    }

    @Test
    public void testMessageLengthFailure() {
        // Message exceeding 250 characters should fail
        String longMessage = "This is a very long message that is definitely going to exceed the two hundred and fifty character limit "
                + "that has been set for this chat application. We need to make sure that the validation catches this "
                + "and returns the correct error message to the user with the number of excess characters.";
        String result = Message.validateMessageLength(longMessage);
        int excess = longMessage.length() - 250;
        assertEquals("Message exceeds 250 characters by " + excess + "; please reduce the size.", result);
    }

    // -----------------------------------------------
    // Tests: Recipient cell number validation
    // -----------------------------------------------

    @Test
    public void testRecipientCellCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientCellIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell("08575975889"));
    }

    // -----------------------------------------------
    // Tests: Message hash generation
    // -----------------------------------------------

    @Test
    public void testMessageHashCorrect() {
        // Test data from Part 2 spec:
        // Message: "Hi Mike, can you join us for dinner tonight?"
        // Expected hash: 00:0:HITONIGHT
        // We need an ID that starts with "00" to match the expected output
        String testID = "0012345678";
        String testMessage = "Hi Mike, can you join us for dinner tonight?";
        int testMessageNum = 0;
        String hash = message.createMessageHash(testID, testMessageNum, testMessage);
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testMessageHashesInLoop() {
        // Tests all message hashes from the spec test data in a loop
        String[] messageIDs = {"0012345678", "0098765432"};
        int[] messageNumbers = {0, 1};
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT"
        };

        for (int i = 0; i < messages.length; i++) {
            String result = message.createMessageHash(messageIDs[i], messageNumbers[i], messages[i]);
            assertEquals(expectedHashes[i], result);
        }
    }

    // -----------------------------------------------
    // Tests: Message ID generation
    // -----------------------------------------------

    @Test
    public void testMessageIDGenerated() {
        String id = Message.generateMessageID();
        System.out.println("Message ID generated: " + id);
        // ID must be exactly 10 characters
        assertTrue(message.checkMessageID(id));
        assertEquals(10, id.length());
    }

    // -----------------------------------------------
    // Tests: sentMessage options
    // -----------------------------------------------

    @Test
    public void testSendMessageOption() {
        assertEquals("Message successfully sent.", message.sentMessage("1"));
    }

    @Test
    public void testDisregardMessageOption() {
        assertEquals("Press 0 to delete the message.", message.sentMessage("2"));
    }

    @Test
    public void testStoreMessageOption() {
        assertEquals("Message successfully stored.", message.sentMessage("3"));
    }
}
