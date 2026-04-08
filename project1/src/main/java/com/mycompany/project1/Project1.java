package com.mycompany.project1;

import java.util.Scanner;

public class Project1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Registration ===");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Cell Phone Number (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine();

        Login user = new Login(firstName, lastName, username, password, cellPhone);

        System.out.println(user.registerUser());

        System.out.println("\n=== Login ===");
        System.out.print("Enter Username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Enter Password: ");
        String enteredPassword = scanner.nextLine();

        System.out.println(user.returnLoginStatus(enteredUsername, enteredPassword));
    }
}