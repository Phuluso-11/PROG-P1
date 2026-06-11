// Part 3 - MessageDataTest class
// Author: Phuluso Kone
// Date: 10 June 2026
// Purpose: JUnit 5 unit tests for MessageData - Part 3 of QuickChatApp
// Tests use the exact test data provided in the Part 3 specification.
package com.mycompany.quickchatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageDataTest {

    // MessageData instance reset before each test for test isolation
    private MessageData messageData;

    // -------------------------
    // Test data as per Part 3 specification
    // -------------------------
    // Message 1: Recipient +27834557896, "Did you get the cake?", Sent
    // Message 2: Recipient +27838884567, "Where are you? You are late! I have asked you to be on time.", Stored
    // Message 3: Recipient +27834484567, "Yohoooo, I am at your gate.", Disregard
    // Message 4: Recipient 0838884567,   "It is dinner time !", Sent
    // Message 5: Recipient +27838884567, "Ok, I am leaving without you.", Stored

    @BeforeEach
    public void setUp() {
        messageData = new MessageData();

        Message handler = new Message();

        // Message 1
        String id1   = "1234567890";
        String hash1 = handler.createMessageHash(id1, 0, "Did you get the cake?");
        Message m1   = new Message(id1, 0, "+27834557896", "Did you get the cake?", hash1, "Sent");

        // Message 2
        String id2   = "2345678901";
        String hash2 = handler.createMessageHash(id2, 1,
                       "Where are you? You are late! I have asked you to be on time.");
        Message m2   = new Message(id2, 1, "+27838884567",
                       "Where are you? You are late! I have asked you to be on time.", hash2, "Stored");

        // Message 3
        String id3   = "3456789012";
        String hash3 = handler.createMessageHash(id3, 2, "Yohoooo, I am at your gate.");
        Message m3   = new Message(id3, 2, "+27834484567", "Yohoooo, I am at your gate.", hash3, "Disregarded");

        // Message 4
        String id4   = "0838884567";
        String hash4 = handler.createMessageHash(id4, 3, "It is dinner time !");
        Message m4   = new Message(id4, 3, "0838884567", "It is dinner time !", hash4, "Sent");

        // Message 5
        String id5   = "5678901234";
        String hash5 = handler.createMessageHash(id5, 4, "Ok, I am leaving without you.");
        Message m5   = new Message(id5, 4, "+27838884567", "Ok, I am leaving without you.", hash5, "Stored");

        messageData.addMessage(m1);
        messageData.addMessage(m2);
        messageData.addMessage(m3);
        messageData.addMessage(m4);
        messageData.addMessage(m5);
    }

    // -------------------------
    // Test 1: Sent Messages array correctly populated
    // Spec: "Did you get the cake?", "It is dinner time!" should appear in sent list
    // -------------------------
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertTrue(messageData.getSentMessagesList().contains("Did you get the cake?"),
                   "Sent messages should contain 'Did you get the cake?'");
        assertTrue(messageData.getSentMessagesList().contains("It is dinner time !"),
                   "Sent messages should contain 'It is dinner time !'");
    }

    // -------------------------
    // Test 2: Display the longest message
    // Spec: Should return message 2 — the longest by character count
    // -------------------------
    @Test
    public void testDisplayLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        String actual   = messageData.displayLongestMessage();
        assertEquals(expected, actual,
                     "Longest message should be message 2");
    }

    // -------------------------
    // Test 3: Search for message by ID
    // Spec: Search "0838884567" → returns "It is dinner time !"
    // -------------------------
    @Test
    public void testSearchByMessageID() {
        String result = messageData.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time !"),
                   "Search by ID '0838884567' should return 'It is dinner time !'");
    }

    // -------------------------
    // Test 4: Search all messages sent to a particular recipient
    // Spec: Search "+27838884567" → returns messages 2 and 5
    // -------------------------
    @Test
    public void testSearchByRecipient() {
        String result = messageData.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."),
                   "Recipient search should include message 2");
        assertTrue(result.contains("Ok, I am leaving without you."),
                   "Recipient search should include message 5");
    }

    // -------------------------
    // Test 5: Delete a message using its hash
    // Spec: Delete message 2 — confirm it is removed and correct text is reported
    // -------------------------
    @Test
    public void testDeleteMessageByHash() {
        // Get the hash of message 2 from the loaded data
        String hashToDelete = null;
        for (Message m : messageData.getAllMessages()) {
            if (m.getMessageText().equals(
                    "Where are you? You are late! I have asked you to be on time.")) {
                hashToDelete = m.getMessageHash();
                break;
            }
        }

        assertNotNull(hashToDelete, "Hash for message 2 should be found");

        String result = messageData.deleteMessage(hashToDelete);

        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time"),
                   "Delete result should confirm the correct message was deleted");
        assertTrue(result.contains("successfully deleted"),
                   "Delete result should say 'successfully deleted'");

        // Confirm it is no longer in the stored list
        assertFalse(messageData.getStoredMessagesList()
                        .contains("Where are you? You are late! I have asked you to be on time."),
                    "Deleted message should no longer appear in stored list");
    }

    // -------------------------
    // Test 6: Display Report
    // Spec: Report should show message hash, recipient, and message for all messages
    // -------------------------
    @Test
    public void testDisplayReport() {
        String report = messageData.displayReport();

        assertTrue(report.contains("Message Hash:"), "Report should include 'Message Hash:'");
        assertTrue(report.contains("Recipient:"),    "Report should include 'Recipient:'");
        assertTrue(report.contains("Message:"),      "Report should include 'Message:'");

        // Verify actual message data appears in the report
        assertTrue(report.contains("Did you get the cake?"),
                   "Report should contain message 1 text");
        assertTrue(report.contains("+27834557896"),
                   "Report should contain message 1 recipient");
    }
}
