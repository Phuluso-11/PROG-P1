// Part 2 - Message class
// Author: Phuluso Kone
// Date: 23 May 2026
// Purpose: Message class for QuickChatApp - Part 2 (Sending Messages)
package com.mycompany.quickchatapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    // Fields for each message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status; // "Sent", "Stored", "Disregarded"

    // Static list to hold all sent messages during the session
    // Reference: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Default constructor
    public Message() {
    }

    // Parameterised constructor
    public Message(String messageID, int messageNumber, String recipient, String messageText, String messageHash, String status) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = status;
    }

    // -------------------------
    // Getters
    // -------------------------
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }

    // validates message ID and recipient cell number
    // Method: checkMessageID
    // Ensures the message ID is not more than 10 characters
    
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    
    // Method: checkRecipientCell
    // Ensures recipient cell number is no more than 10 chars (excl. +) and starts with international code
    // Reuses same regex logic from Login class
    // Reference: https://www.regular-expressions.info/
    
    public String checkRecipientCell(String cellNumber) {
        String regex = "^(\\+27|0)[6-8][0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellNumber);
        if (matcher.matches()) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // creates message hash and handles send options
    // Method: createMessageHash
    // Format: first 2 digits of ID : message number : FIRSTWORD + LASTWORD (all caps)
    // Example: 00:0:HITONIGHT
    
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        // Get first two characters of the message ID
        String idPrefix = messageID.substring(0, 2);

        // Get first and last word of the message
        String trimmed = messageText.trim();
        String[] words = trimmed.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        // Remove any punctuation from last word
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");

        // Build and return hash in all caps
        String hash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    
    // Method: sentMessage
    // Allows user to choose send, store, or disregard
    
    public String sentMessage(String choice) {
        switch (choice.trim()) {
            case "1":
                return "Message successfully sent.";
            case "2":
                return "Press 0 to delete the message.";
            case "3":
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    
    // Method: printMessages
    // Returns all messages sent while the program is running
    
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("-----------------------------\n");
            sb.append("Message ID:   ").append(m.getMessageID()).append("\n");
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient:    ").append(m.getRecipient()).append("\n");
            sb.append("Message:      ").append(m.getMessageText()).append("\n");
            sb.append("Status:       ").append(m.getStatus()).append("\n");
        }
        sb.append("-----------------------------");
        return sb.toString();
    }

    
    // Method: returnTotalMessages
    // Returns total number of messages sent (not stored or disregarded)
    
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    
    // Method: addToSentList (helper)
    // Adds a message to the static sent list
    
    public static void addToSentList(Message m) {
        sentMessages.add(m);
        if (m.getStatus().equals("Sent")) {
            totalMessagesSent++;
        }
    }

    // stores messages to JSON and prints all messages
    // Method: storeMessage
    // Stores a message to a JSON file
    // Reference: https://github.com/fangyidong/json-simple (JSON.simple library)
    
    @SuppressWarnings("unchecked")
    public void storeMessage(Message m) {
        JSONObject messageObj = new JSONObject();
        messageObj.put("messageID", m.getMessageID());
        messageObj.put("messageNumber", m.getMessageNumber());
        messageObj.put("recipient", m.getRecipient());
        messageObj.put("messageText", m.getMessageText());
        messageObj.put("messageHash", m.getMessageHash());
        messageObj.put("status", m.getStatus());

        // Read existing file or start fresh array
        JSONArray messageArray = new JSONArray();
        messageArray.add(messageObj);

        try (FileWriter file = new FileWriter("storedMessages.json", true)) {
            file.write(messageObj.toJSONString());
            file.write(System.lineSeparator());
            System.out.println("Message successfully stored.");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // -------------------------
    // Static helper: generateMessageID
    // Generates a random 10-digit message ID
    // -------------------------
    public static String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // -------------------------
    // Static helper: validateMessageLength
    // Checks message is within 250 characters
    // -------------------------
    public static String validateMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }
}
