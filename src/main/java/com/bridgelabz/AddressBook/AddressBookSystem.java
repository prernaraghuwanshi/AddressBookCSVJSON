package com.bridgelabz.AddressBook;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookSystem {

    public enum IOType {
        FILE_IO, CSV_IO, JSON_IO, DB_IO
    }

    ;
    public static List<Contacts> contactsList;
    private static AddressBookDBService addressBookDBService;

    HashMap<String, AddressBookMain> addressBookMap;
    private Scanner sc;

    // Constructor
    public AddressBookSystem() {
        addressBookMap = new HashMap<String, AddressBookMain>();
        addressBookDBService = AddressBookDBService.getInstance();
    }

    // Add address book to Address Book System
    public void addAddressBook(String name) {
        AddressBookMain a = new AddressBookMain();
        boolean flag = true;
        while (flag) {
            System.out.println(
                    "Choose action on Address Book \n\n 1. Add Contact \n 2. Edit Contact \n 3. Delete Contact \n 4. Display Address Book \n 5. Exit \n");
            sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    // Add contact
                    a.addContact();
                    break;
                case 2:
                    // Edit contact
                    System.out.println("Enter first name of contact to be edited: ");
                    Scanner s = new Scanner(System.in);
                    String firstNameEdit = s.nextLine();
                    a.editContact(firstNameEdit);
                    break;
                case 3:
                    // Delete Contact
                    System.out.println("Enter first name of contact to be deleted: ");
                    Scanner s1 = new Scanner(System.in);
                    String firstNameDelete = s1.nextLine();
                    a.deleteContact(firstNameDelete);
                    break;
                case 4:
                    // Display
                    for (int i = 0; i < a.contactList.size(); i++) {
                        a.displayContact(i);
                    }
                    break;
                case 5:
                    // Exit
                    System.out.println("-----Exiting from Address Book-----");
                    flag = false;
                    break;
            }
        }
        addressBookMap.put(name, a);
        System.out.println("-----Address Book " + name + " added-----");
    }

    // Get person by city (without using hashMap)
    public void getPersonByCity(String cityName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> contactsList = addr.contactList.stream().filter(contact -> contact.city.equals(cityName))
                    .collect(Collectors.toList());
            System.out.println("Person from " + cityName + " in Address Book " + entry.getKey() + " are: ");
            contactsList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Get person by state (without using hashMap)
    public void getPersonByState(String stateName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> contactsList = addr.contactList.stream().filter(contact -> contact.state.equals(stateName))
                    .collect(Collectors.toList());
            System.out.println("Person from " + stateName + " in Address Book " + entry.getKey() + " are: ");
            contactsList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // View person by city (using hashMap)
    public void viewPersonByCity(String cityName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            ArrayList<String> names = addr.cityMap.entrySet().stream().filter(str -> str.getKey().equals(cityName))
                    .map(Map.Entry::getValue).findFirst().orElse(null);
            if (names == null) {
                System.out.println("City does not exist!");
                return;
            }
            System.out.println(names);
        }
    }

    // View person by city (using hashMap)
    public void viewPersonByState(String stateName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            ArrayList<String> names = addr.stateMap.entrySet().stream().filter(str -> str.getKey().equals(stateName))
                    .map(Map.Entry::getValue).findFirst().orElse(null);
            if (names == null) {
                System.out.println("State does not exist!");
                return;
            }
            System.out.println(names);
        }
    }

    // Count of all contacts in a city
    public void countByCity(String cityName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            Integer count = (int) addr.cityMap.entrySet().stream().filter(str -> str.getKey().equals(cityName))
                    .map(Map.Entry::getValue).findFirst().orElse(null).size();
            System.out.println("Count for address book " + entry.getKey() + " is: " + count);
        }
    }

    // Count of all contacts in a state
    public void countByState(String stateName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            Integer count = (int) addr.stateMap.entrySet().stream().filter(str -> str.getKey().equals(stateName))
                    .map(Map.Entry::getValue).findFirst().orElse(null).size();
            System.out.println("Count for address book " + entry.getKey() + " is: " + count);
        }
    }

    // Sort by name in alphabetical order
    public void sortByName() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contacts::getFirstName))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by city in alphabetical order
    public void sortByCity() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contacts::getCity))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by state in alphabetical order
    public void sortByState() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contacts::getState))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by ZIP in alphabetical order
    public void sortByZip() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contacts> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contacts::getZip))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    public void writeData(String addressBookName, IOType iotype) {
        if (iotype.equals(IOType.FILE_IO))
            new AddressBookIO().writeDataToFile(addressBookMap.get(addressBookName).contactList, addressBookName);
        else if (iotype.equals(IOType.CSV_IO))
            try {
                new AddressBookIO().writeDataToCSV(addressBookMap.get(addressBookName).contactList, addressBookName);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            }
        else if (iotype.equals(IOType.JSON_IO))
            try {
                new AddressBookIO().writeDataToJSON(addressBookMap.get(addressBookName).contactList, addressBookName);
            } catch (IOException e) {
            }
    }

    public ArrayList<Contacts> readData(String addressBookName, IOType iotype) throws SQLException {
        if (iotype.equals(IOType.FILE_IO)) {
            ArrayList<Contacts> readFromFileContactList = new AddressBookIO().readDataFromFile(addressBookName);
            return readFromFileContactList;
        } else if (iotype.equals(IOType.CSV_IO)) {
            ArrayList<Contacts> readFromCSVContactList = null;
            try {
                readFromCSVContactList = new AddressBookIO().readDataFromCSV(addressBookName);
            } catch (IOException e) {
            }
            return readFromCSVContactList;
        } else if (iotype.equals(IOType.JSON_IO)) {
            ArrayList<Contacts> readFromJSONContactList = null;
            try {
                readFromJSONContactList = new AddressBookIO().readDataFromJSON(addressBookName);
            } catch (IOException e) {
            }
            return readFromJSONContactList;
        } else if (iotype.equals(IOType.DB_IO)) {
           return (ArrayList<Contacts>) readDataFromDB();
        }
        return null;
    }

    public List<Contacts> readDataFromDB() throws SQLException {
        contactsList = addressBookDBService.readData();
        return contactsList;
    }

}
