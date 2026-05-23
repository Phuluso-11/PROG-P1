// Author: Phuluso Kone
// Date: 23 May 2026
// Purpose: Main application class for QuickChatApp - Part 1 + Part 2
package com.mycompany.quickchatapp;

import java.util.Scanner;

public class QuickChatApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        
        // REGISTRATION
        
        System.out.println("=== Registration ===");

        System.out.print("Please enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Please enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Please enter your username: ");
        String username = input.nextLine();

        System.out.print("Please enter your password: ");
        String password = input.nextLine();

        System.out.print("Please enter your cellphone number: ");
        String cellphoneNum = input.nextLine();

        String registrationResult = login.registerUser(firstName, lastName, username, password, cellphoneNum);
        System.out.println(registrationResult);

        // If registration failed, stop the app
        if (!registrationResult.startsWith("Welcome")) {
            System.out.println("Registration failed. Please restart the application and try again.");
            input.close();
            return;
        }

        // -------------------------
        // LOGIN
        // -------------------------
        System.out.println("\n=== Login ===");
        System.out.print("Please enter your username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Please enter your password: ");
        String enteredPassword = input.nextLine();

        String loginResult = login.returnLoginStatus(enteredUsername, enteredPassword);
        System.out.println(loginResult);

        // Only proceed to messaging if login was successful
        if (!login.loginUser(enteredUsername, enteredPassword)) {
            System.out.println("Login failed. Please restart the application and try again.");
            input.close();
            return;
        }

        // Part 2 - messaging menu added
        
        
        System.out.println("\nWelcome to QuickChat.");

        // Ask user how many messages they want to send
        int numMessages = 0;
        while (numMessages <= 0) {
            System.out.print("How many messages would you like to send? ");
            try {
                numMessages = Integer.parseInt(input.nextLine().trim());
                if (numMessages <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Message messageHandler = new Message();
        String menuChoice = "";

        // Main application loop - runs until user selects Quit
        while (!menuChoice.equals("3")) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Please select an option: ");
            menuChoice = input.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    // For loop to allow user to send the set number of messages
                    for (int i = 0; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

                        // Generate unique 10-digit message ID
                        String messageID = Message.generateMessageID();
                        System.out.println("Message ID generated: " + messageID);

                        // Message number (1-based, uses loop counter)
                        int messageNumber = i;

                        // Get recipient number
                        String recipient = "";
                        while (true) {
                            System.out.print("Enter recipient cell number (with international code): ");
                            recipient = input.nextLine().trim();
                            String cellCheck = messageHandler.checkRecipientCell(recipient);
                            System.out.println(cellCheck);
                            if (cellCheck.equals("Cell phone number successfully captured.")) {
                                break;
                            }
                        }

                        // Get message text (max 250 characters)
                        String messageText = "";
                        while (true) {
                            System.out.print("Enter your message (max 250 characters): ");
                            messageText = input.nextLine();
                            String lengthCheck = Message.validateMessageLength(messageText);
                            if (lengthCheck.equals("Message ready to send.")) {
                                System.out.println("Message ready to send.");
                                break;
                            } else {
                                System.out.println(lengthCheck);
                            }
                        }

                        // Auto-generate the message hash
                        String hash = messageHandler.createMessageHash(messageID, messageNumber, messageText);
                        System.out.println("Message Hash: " + hash);

                        // Ask user what to do with the message
                        System.out.println("\nWhat would you like to do with this message?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Please select an option: ");
                        String sendChoice = input.nextLine().trim();

                        String sendResult = messageHandler.sentMessage(sendChoice);
                        System.out.println(sendResult);

                        // Determine status and store/record accordingly
                        String status;
                        switch (sendChoice) {
                            case "1":
                                status = "Sent";
                                break;
                            case "2":
                                status = "Disregarded";
                                break;
                            case "3":
                                status = "Stored";
                                break;
                            default:
                                status = "Unknown";
                        }

                        // Create the message object and add to list
                        Message newMessage = new Message(messageID, messageNumber, recipient, messageText, hash, status);
                        Message.addToSentList(newMessage);

                        // Store to JSON if user chose to store
                        if (sendChoice.equals("3")) {
                            messageHandler.storeMessage(newMessage);
                        }

                        // Display full message details after sending
                        if (sendChoice.equals("1")) {
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID:   " + messageID);
                            System.out.println("Message Hash: " + hash);
                            System.out.println("Recipient:    " + recipient);
                            System.out.println("Message:      " + messageText);
                        }
                    }

                    // Display total messages sent after the loop completes
                    System.out.println("\nTotal messages sent: " + messageHandler.returnTotalMessages());
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Goodbye! Thank you for using QuickChat.");
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2, or 3.");
            }
        }

        input.close();
    }
}
