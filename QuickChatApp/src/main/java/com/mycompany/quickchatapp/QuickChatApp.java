// Author: Phuluso Kone
// Date: 10 June 2026
// Purpose: Main application class for QuickChatApp - Part 1 + Part 2 + Part 3
package com.mycompany.quickchatapp;

import java.util.Scanner;

public class QuickChatApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // -------------------------
        // REGISTRATION
        // -------------------------
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

        if (!login.loginUser(enteredUsername, enteredPassword)) {
            System.out.println("Login failed. Please restart the application and try again.");
            input.close();
            return;
        }

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
        MessageData messageData = new MessageData();

        // Load any previously stored messages from JSON into the arrays
        messageData.loadStoredMessagesFromJSON("storedMessages.json");

        String menuChoice = "";

        // -------------------------
        // MAIN MENU LOOP
        // -------------------------
        while (!menuChoice.equals("4")) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Please select an option: ");
            menuChoice = input.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    // Send messages loop
                    for (int i = 0; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

                        String messageID = Message.generateMessageID();
                        System.out.println("Message ID generated: " + messageID);

                        int messageNumber = i;

                        // Get and validate recipient
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

                        // Get and validate message text
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

                        String hash = messageHandler.createMessageHash(messageID, messageNumber, messageText);
                        System.out.println("Message Hash: " + hash);

                        System.out.println("\nWhat would you like to do with this message?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Please select an option: ");
                        String sendChoice = input.nextLine().trim();

                        String sendResult = messageHandler.sentMessage(sendChoice);
                        System.out.println(sendResult);

                        String status;
                        switch (sendChoice) {
                            case "1":  status = "Sent";        break;
                            case "2":  status = "Disregarded"; break;
                            case "3":  status = "Stored";      break;
                            default:   status = "Unknown";
                        }

                        Message newMessage = new Message(messageID, messageNumber, recipient,
                                                         messageText, hash, status);
                        Message.addToSentList(newMessage);

                        // Add to our Part 3 data arrays
                        messageData.addMessage(newMessage);

                        if (sendChoice.equals("3")) {
                            messageHandler.storeMessage(newMessage);
                        }

                        if (sendChoice.equals("1")) {
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID:   " + messageID);
                            System.out.println("Message Hash: " + hash);
                            System.out.println("Recipient:    " + recipient);
                            System.out.println("Message:      " + messageText);
                        }
                    }

                    System.out.println("\nTotal messages sent: " + messageHandler.returnTotalMessages());
                    break;

                case "2":
                    System.out.println(messageHandler.printMessages());
                    break;

                // -------------------------
                // OPTION 3: Stored Messages sub-menu (Part 3)
                // -------------------------
                case "3":
                    String subChoice = "";
                    while (!subChoice.equals("7")) {
                        System.out.println("\n--- Stored Messages Menu ---");
                        System.out.println("1) Display sender and recipient of all stored messages");
                        System.out.println("2) Display the longest message");
                        System.out.println("3) Search by Message ID");
                        System.out.println("4) Search messages by recipient");
                        System.out.println("5) Delete a message by hash");
                        System.out.println("6) Display full message report");
                        System.out.println("7) Back to main menu");
                        System.out.print("Please select an option: ");
                        subChoice = input.nextLine().trim();

                        switch (subChoice) {
                            case "1":
                                System.out.println(messageData.displayStoredSenderRecipient());
                                break;

                            case "2":
                                System.out.println("Longest Message: " + messageData.displayLongestMessage());
                                break;

                            case "3":
                                System.out.print("Enter Message ID to search: ");
                                String searchID = input.nextLine().trim();
                                System.out.println(messageData.searchByMessageID(searchID));
                                break;

                            case "4":
                                System.out.print("Enter recipient cell number to search: ");
                                String searchRecipient = input.nextLine().trim();
                                System.out.println(messageData.searchByRecipient(searchRecipient));
                                break;

                            case "5":
                                System.out.print("Enter message hash to delete: ");
                                String deleteHash = input.nextLine().trim();
                                System.out.println(messageData.deleteMessage(deleteHash));
                                break;

                            case "6":
                                System.out.println(messageData.displayReport());
                                break;

                            case "7":
                                System.out.println("Returning to main menu...");
                                break;

                            default:
                                System.out.println("Invalid option. Please select 1-7.");
                        }
                    }
                    break;

                case "4":
                    System.out.println("Goodbye! Thank you for using QuickChat.");
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2, 3, or 4.");
            }
        }

        input.close();
    }
}
