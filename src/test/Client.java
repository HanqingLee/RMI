package test;

import rmi.*;
import java.util.Scanner;

public class Client {

	private RMIPractice stub = null;

	public static void main(String args[]) {
		String username = "hanson";
		String pw = "test";
		Scanner input = new Scanner(System.in);
		System.out.println("For Log in, please enter 1. For register, please enter 2");
		String temp = input.next();
		// String temp = "1";

		System.out.println("Please enter the username:");
		username = input.next();
		System.out.println("Please enter the password:");
		pw = input.next();
		input.close();
		
		// Encrypt the password
		pw = CreateMD5.getMd5(pw);

		Client client = new Client();
		if (temp.equals("2"))
			client.Register(username, pw);
		else if (temp.equals("1"))
			client.Login(username, pw);
		else
			System.out.println("Error!");

	}

	// invoke the remote register function
	void Register(String username, String pw) {
		try {
			// Set the target host to the localhost, and port be 2017
			Registry reg = new Registry("127.0.0.1", 2017);
			stub = (RMIPractice) reg.lookup("RMI_HH");

			// invoke the remote register method, and get the operation status
			// returned by the function
			int result = stub.Register(username, pw);
			if (result == -1)
				System.out.println("Failed! Cannot connect to database!");
			else if (result == 0)
				System.out.println("Successfully registered!");
			else if (result == 1)
				System.out.println("Failed! Cannot add user to database!");
			else if (result == 2)
				System.out.println("Failed! The user is already exist!");
		} catch (Exception e) {
			System.out.println("Lookup Error!");
		}
	}

	// invoke the remote login function
	void Login(String username, String pw) {
		try {
			// Set the target host to the localhost, and port be 2017
			Registry reg = new Registry("127.0.0.1", 2017);
			stub = (RMIPractice) reg.lookup("RMI_HH");

			// invoke the remote register method, and get the operation status
			// returned by the function
			int result = stub.Login(username, pw);
			if (result == -1)
				System.out.println("Failed! Cannot connect to database!");
			else if (result == 0)
				System.out.println("Login success!");
			else if (result == 1)
				System.out.println("Failed! Password is incorrect!");
			else if (result == 2)
				System.out.println("Failed! User is not in existence!");
		} catch (Exception e) {
			System.err.println("Client exception thrown: " + e.toString());
			e.printStackTrace();
		}
	}
}
