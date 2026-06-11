// Part 3 - MessageData class
// Author: Phuluso Kone
// Date: 10 June 2026
// Purpose: Manages parallel arrays and all data operations for Part 3 of QuickChatApp
// Stores sent, stored, and disregarded messages and provides search/delete/report features.
package com.mycompany.quickchatapp;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class MessageData {

    // -------------------------
    // Parallel arrays
    // -------------------------
    // Each index position corresponds to the same message across all arrays.
    // "Parallel arrays" means index 0 in sentMessagesList matches index 0 in messageHashesList, etc.
    private ArrayList<String> sentMessagesList       = new ArrayList<>();
    private ArrayList<String> disregardedMessagesList = new ArrayList<>();
    private ArrayList<String> storedMessagesList     = new ArrayList<>();
    private ArrayList<String> messageHashesList      = new ArrayList<>();
    private ArrayList<String> messageIDsList         = new ArrayList<>();

    // Full message objects stored per category for richer lookups
    private ArrayList<Message> allMessages = new ArrayList<>();

    // -------------------------
    // Populate arrays from a Message object
    // Called each time a message is sent, stored, or disregarded
    // -------------------------
    public void addMessage(Message m) {
        allMessages.add(m);
        messageHashesList.add(m.getMessageHash());
        messageIDsList.add(m.getMessageID());

        switch (m.getStatus()) {
            case "Sent":
                sentMessagesList.add(m.getMessageText());
                break;
            case "Stored":
                storedMessagesList.add(m.getMessageText());
                break;
            case "Disregarded":
                disregardedMessagesList.add(m.getMessageText());
                break;
        }
    }

    // -------------------------
    // a. Display sender and recipient of all stored messages
    // -------------------------
    public String displayStoredSenderRecipient() {
        if (allMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Stored Message Senders & Recipients ===\n");
        boolean found = false;

        for (Message m : allMessages) {
            if (m.getStatus().equals("Stored")) {
                sb.append("Sender:    You\n");
                sb.append("Recipient: ").append(m.getRecipient()).append("\n");
                sb.append("Message:   ").append(m.getMessageText()).append("\n");
                sb.append("-----------------------------\n");
                found = true;
            }
        }

        if (!found) {
            return "No stored messages found.";
        }
        return sb.toString();
    }

    // -------------------------
    // b. Display the longest stored message
    // Searches the stored messages list and returns the longest one
    // -------------------------
    public String displayLongestMessage() {
        if (allMessages.isEmpty()) {
            return "No messages found.";
        }

        String longest = "";
        for (Message m : allMessages) {
            if (m.getMessageText().length() > longest.length()) {
                longest = m.getMessageText();
            }
        }

        return longest.isEmpty() ? "No messages found." : longest;
    }

    // -------------------------
    // c. Search for a message by ID and display recipient + message
    // -------------------------
    public String searchByMessageID(String id) {
        for (Message m : allMessages) {
            if (m.getMessageID().equals(id)) {
                return "Recipient: " + m.getRecipient() + "\nMessage:   " + m.getMessageText();
            }
        }
        return "Message ID " + id + " not found.";
    }

    // -------------------------
    // d. Search all messages for a particular recipient
    // Returns all sent or stored messages matching that recipient's number
    // -------------------------
    public String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        for (Message m : allMessages) {
            if (m.getRecipient().equals(recipient)
                    && (m.getStatus().equals("Sent") || m.getStatus().equals("Stored"))) {
                sb.append("\"").append(m.getMessageText()).append("\"").append("\n");
                found = true;
            }
        }

        return found ? sb.toString().trim() : "No messages found for recipient: " + recipient;
    }

    // -------------------------
    // e. Delete a message using the message hash
    // Removes from allMessages list and from the relevant category list
    // -------------------------
    public String deleteMessage(String hash) {
        for (int i = 0; i < allMessages.size(); i++) {
            Message m = allMessages.get(i);
            if (m.getMessageHash().equals(hash)) {
                String text = m.getMessageText();
                String status = m.getStatus();

                // Remove from parallel arrays
                messageHashesList.remove(m.getMessageHash());
                messageIDsList.remove(m.getMessageID());

                switch (status) {
                    case "Sent":
                        sentMessagesList.remove(text);
                        break;
                    case "Stored":
                        storedMessagesList.remove(text);
                        break;
                    case "Disregarded":
                        disregardedMessagesList.remove(text);
                        break;
                }

                allMessages.remove(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Hash not found. No message deleted.";
    }

    // -------------------------
    // f. Display a full report of all stored messages
    // Shows message hash, recipient, and message text
    // -------------------------
    public String displayReport() {
        if (allMessages.isEmpty()) {
            return "No messages to report.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         QUICKCHAT MESSAGE REPORT       \n");
        sb.append("========================================\n");

        for (Message m : allMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient:    ").append(m.getRecipient()).append("\n");
            sb.append("Message:      ").append(m.getMessageText()).append("\n");
            sb.append("Status:       ").append(m.getStatus()).append("\n");
            sb.append("----------------------------------------\n");
        }

        return sb.toString();
    }

    // -------------------------
    // Load stored messages from JSON file into the arrays
    // Reference: https://github.com/fangyidong/json-simple
    // -------------------------
    public void loadStoredMessagesFromJSON(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // File is written as one JSON object per line
            // Read each line as a separate JSON object
            java.io.BufferedReader br = new java.io.BufferedReader(reader);
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    JSONObject obj = (JSONObject) parser.parse(line);

                    String messageID   = (String) obj.get("messageID");
                    long msgNumLong    = obj.get("messageNumber") != null
                                        ? (long) obj.get("messageNumber") : 0;
                    int messageNumber  = (int) msgNumLong;
                    String recipient   = (String) obj.get("recipient");
                    String messageText = (String) obj.get("messageText");
                    String messageHash = (String) obj.get("messageHash");
                    String status      = (String) obj.get("status");

                    Message m = new Message(messageID, messageNumber, recipient,
                                            messageText, messageHash, status);
                    addMessage(m);

                } catch (ParseException e) {
                    System.out.println("Skipping malformed JSON line: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("No stored messages file found or error reading: " + e.getMessage());
        }
    }

    // -------------------------
    // Getters for unit tests
    // -------------------------
    public ArrayList<String> getSentMessagesList()        { return sentMessagesList; }
    public ArrayList<String> getDisregardedMessagesList() { return disregardedMessagesList; }
    public ArrayList<String> getStoredMessagesList()      { return storedMessagesList; }
    public ArrayList<String> getMessageHashesList()       { return messageHashesList; }
    public ArrayList<String> getMessageIDsList()          { return messageIDsList; }
    public ArrayList<Message> getAllMessages()             { return allMessages; }
}
