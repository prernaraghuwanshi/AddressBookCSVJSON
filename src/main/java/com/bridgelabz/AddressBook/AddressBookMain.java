package com.bridgelabz.AddressBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddressBookMain {
	Scanner sc = new Scanner(System.in);
	public ArrayList<Contacts> contactList;
	public HashMap<String, Contacts> contactMap;
	HashMap<String, ArrayList<String>> cityMap;
	HashMap<String, ArrayList<String>> stateMap;

	// Constructor
	public AddressBookMain() {
		contactList = new ArrayList<Contacts>();
		contactMap = new HashMap<String, Contacts>();
		cityMap = new HashMap<String, ArrayList<String>>();
		stateMap = new HashMap<String, ArrayList<String>>();
	}

	// Add contact to Address Book
	public void addContact() {
		System.out.println("-----Add Contact Details-----");
		System.out.println("Enter First Name: ");
		String firstName = sc.nextLine();
		List<String> names = contactList.stream().map(Contacts::getFirstName).collect(Collectors.toList());
		boolean checkDuplicateName = names.stream().anyMatch(name -> firstName.equals(name));
		if (checkDuplicateName == true) {
			System.out.println("First Name exists in address book!");
			return;
		}
		System.out.println("Enter Last Name: ");
		String lastName = sc.nextLine();
		System.out.println("Enter Address: ");
		String address = sc.nextLine();
		System.out.println("Enter City: ");
		String city = sc.nextLine();
		System.out.println("Enter State:");
		String state = sc.nextLine();
		System.out.println("Enter ZIP Code: ");
		String zip = sc.nextLine();
		System.out.println("Enter Phone Number: ");
		String phone = sc.nextLine();
		System.out.println("Enter Email Address: ");
		String email = sc.nextLine();
		Contacts c = new Contacts(firstName, lastName, address, city, state, zip, phone, email);
		contactList.add(c);
		contactMap.put(firstName, c);
		if (!cityMap.isEmpty() && cityMap.containsKey(city)) {
			ArrayList<String> nameList = cityMap.get(city);
			nameList.add(firstName);
			cityMap.replace(city, nameList);
		} else {
			ArrayList<String> nameList = new ArrayList<String>();
			nameList.add(firstName);
			cityMap.put(city, nameList);
		}
		if (!stateMap.isEmpty() && stateMap.containsKey(state)) {
			ArrayList<String> nameList = stateMap.get(state);
			nameList.add(firstName);
			stateMap.replace(state, nameList);
		} else {
			ArrayList<String> nameList = new ArrayList<String>();
			nameList.add(firstName);
			stateMap.put(state, nameList);
		}
		System.out.println("-----Contact Added Successfully-----");
		System.out.println("Number of contacts in Address Book: " + contactList.size());
	}

	// Edit contact in Address Book when firstName inputted
	public void editContact(String firstNameEdit) {
		System.out.println("-----Edit Contact-----");
		if (contactMap.containsKey(firstNameEdit)) {
			Contacts cedit = contactMap.get(firstNameEdit);
			System.out.println("Enter First Name: ");
			cedit.firstName = sc.nextLine();
			System.out.println("Enter Last Name: ");
			cedit.lastName = sc.nextLine();
			System.out.println("Enter Address: ");
			cedit.address = sc.nextLine();
			System.out.println("Enter City: ");
			cedit.city = sc.nextLine();
			System.out.println("Enter State:");
			cedit.state = sc.nextLine();
			System.out.println("Enter ZIP Code: ");
			cedit.zip = sc.nextLine();
			System.out.println("Enter Phone Number: ");
			cedit.phoneNo = sc.nextLine();
			System.out.println("Enter Email Address: ");
			cedit.email = sc.nextLine();
			contactMap.remove(firstNameEdit);
			contactMap.put(cedit.firstName, cedit);
			System.out.println("-----Contact Details Updated-----");
			System.out.println("Number of contacts in Address Book: " + contactList.size());
		} else
			System.out.println("-----Name not found in Address Book-----");
	}

	// Display A Contact
	public void displayContact(int i) {
		System.out.println("-----Displaying Contact-----");
		System.out.println("First Name: " + contactList.get(i).firstName);
		System.out.println("Last Name: " + contactList.get(i).lastName);
		System.out.println("Address: " + contactList.get(i).address);
		System.out.println("City: " + contactList.get(i).city);
		System.out.println("State: " + contactList.get(i).state);
		System.out.println("ZIP Code: " + contactList.get(i).zip);
		System.out.println("Phone Number: " + contactList.get(i).phoneNo);
		System.out.println("Email: " + contactList.get(i).email);
		System.out.println("  ");
	}

	// Delete a contact be firstName
	public void deleteContact(String firstName) {
		System.out.println("-----Deleting Contact-----");
		if (contactMap.containsKey(firstName)) {
			Contacts c = contactMap.get(firstName);
			contactMap.remove(firstName);
			contactList.remove(c);
			System.out.println("-----Contact Deleted Successfully-----");
			System.out.println("Number of contacts in Address Book: " + contactList.size());
		} else
			System.out.println("-----Name not found in Address Book-----");
	}
}
