package com.mycompany.quickchatapp;

import java.util.Scanner;

public class QuickChatApp {

    public static void main(String[] args) {
        //Scanner object to allow user input
        Scanner input = new Scanner(System.in);

        //login object to allow input validation
        Login login = new Login();

        //declare variables
        String firstName, lastName, username, password, cellphoneNum;

        System.out.println("=== Registration ===");

        System.out.print("Please enter your first name: ");
        firstName = input.nextLine();

        System.out.print("Please enter your last name: ");
        lastName = input.nextLine();

        System.out.print("Please enter your username: ");
        username = input.nextLine();

        System.out.print("Please enter your password: ");
        password = input.nextLine();

        System.out.print("Please enter your cellphone number: ");
        cellphoneNum = input.nextLine();

        System.out.println(login.registerUser(firstName, lastName, username, password, cellphoneNum));

        System.out.println("\n=== Login ===");
        System.out.print("Please enter your username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Please enter your password: ");
        String enteredPassword = input.nextLine();

        System.out.println(login.returnLoginStatus(enteredUsername, enteredPassword));
    }
}