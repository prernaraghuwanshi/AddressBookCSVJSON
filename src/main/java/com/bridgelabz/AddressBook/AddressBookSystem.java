package com.bridgelabz.AddressBook;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBookSystem {

    public enum IOType {
        FILE_IO, CSV_IO, JSON_IO, DB_IO, REST_IO
    }

    public static List<Contact> contactList;
    private static AddressBookDBService addressBookDBService;

    HashMap<String, AddressBookMain> addressBookMap;
    private Scanner sc;

    // Constructor
    public AddressBookSystem() {
        addressBookMap = new HashMap<String, AddressBookMain>();
        addressBookDBService = AddressBookDBService.getInstance();
    }

    //Parameterized Constructor
    public AddressBookSystem(List<Contact> contactList) {
        this();
        this.contactList = new ArrayList<>(contactList);
    }

    // Add address book to Address Book System (exclusively for Console Service)
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

    // Get person by city (without using hashMap)(exclusively for Console Service)
    public void getPersonByCity(String cityName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> contactList = addr.contactList.stream().filter(contact -> contact.city.equals(cityName))
                    .collect(Collectors.toList());
            System.out.println("Person from " + cityName + " in Address Book " + entry.getKey() + " are: ");
            contactList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Get person by state (without using hashMap)(exclusively for Console Service)
    public void getPersonByState(String stateName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> contactList = addr.contactList.stream().filter(contact -> contact.state.equals(stateName))
                    .collect(Collectors.toList());
            System.out.println("Person from " + stateName + " in Address Book " + entry.getKey() + " are: ");
            contactList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // View person by city (using hashMap)(exclusively for Console Service)
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

    // View person by city (using hashMap)(exclusively for Console Service)
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

    // Count of all contacts in a city (exclusively for Console Service)
    public void countByCity(String cityName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            Integer count = (int) addr.cityMap.entrySet().stream().filter(str -> str.getKey().equals(cityName))
                    .map(Map.Entry::getValue).findFirst().orElse(null).size();
            System.out.println("Count for address book " + entry.getKey() + " is: " + count);
        }
    }

    // Count of all contacts in a state (exclusively for Console Service)
    public void countByState(String stateName) {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            Integer count = (int) addr.stateMap.entrySet().stream().filter(str -> str.getKey().equals(stateName))
                    .map(Map.Entry::getValue).findFirst().orElse(null).size();
            System.out.println("Count for address book " + entry.getKey() + " is: " + count);
        }
    }

    // Sort by name in alphabetical order (exclusively for Console Service)
    public void sortByName() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contact::getFirstName))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by city in alphabetical order (exclusively for Console Service)
    public void sortByCity() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contact::getCity))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by state in alphabetical order (exclusively for Console Service)
    public void sortByState() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contact::getState))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Sort by ZIP in alphabetical order (exclusively for Console Service)
    public void sortByZip() {
        for (Map.Entry<String, AddressBookMain> entry : addressBookMap.entrySet()) {
            AddressBookMain addr = entry.getValue();
            List<Contact> sortedList = addr.contactList.stream().sorted(Comparator.comparing(Contact::getZip))
                    .collect(Collectors.toList());
            sortedList.forEach(contact -> System.out.println(contact.firstName + " " + contact.lastName));
        }
    }

    // Write Data to File, CSV file or JSON file
    public void writeData(String addressBookName, IOType iotype) throws IOTypeException {
        if (iotype.equals(IOType.FILE_IO))
            new AddressBookIOService().writeDataToFile(addressBookMap.get(addressBookName).contactList, addressBookName);
        else if (iotype.equals(IOType.CSV_IO))
            try {
                new AddressBookIOService().writeDataToCSV(addressBookMap.get(addressBookName).contactList, addressBookName);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                throw new IOTypeException("CSV File Writing Problem",IOTypeException.ExceptionType.CSV_FILE_ISSUE);
            }
        else if (iotype.equals(IOType.JSON_IO))
            try {
                new AddressBookIOService().writeDataToJSON(addressBookMap.get(addressBookName).contactList, addressBookName);
            } catch (IOException e) {
                throw new IOTypeException("JSON File Writing issue",IOTypeException.ExceptionType.JSON_FILE_ISSUE);
            }
    }

    // Read data from file, CSV file or JSON file
    public ArrayList<Contact> readData(String addressBookName, IOType iotype) throws IOTypeException {
        if (iotype.equals(IOType.FILE_IO)) {
            ArrayList<Contact> readFromFileContactList = new AddressBookIOService().readDataFromFile(addressBookName);
            return readFromFileContactList;
        } else if (iotype.equals(IOType.CSV_IO)) {
            ArrayList<Contact> readFromCSVContactList = null;
            try {
                readFromCSVContactList = new AddressBookIOService().readDataFromCSV(addressBookName);
            } catch (IOException e) {
                throw new IOTypeException("CSV File Reading Problem",IOTypeException.ExceptionType.CSV_FILE_ISSUE);
            }
            return readFromCSVContactList;
        } else if (iotype.equals(IOType.JSON_IO)) {
            ArrayList<Contact> readFromJSONContactList = null;
            try {
                readFromJSONContactList = new AddressBookIOService().readDataFromJSON(addressBookName);
            } catch (IOException e) {
                throw new IOTypeException("JSON File Reading issue",IOTypeException.ExceptionType.JSON_FILE_ISSUE);
            }
            return readFromJSONContactList;
        } else if (iotype.equals(IOType.DB_IO)) {
            return (ArrayList<Contact>) readDataFromDB();
        }
        return null;
    }

    // Read data from Database
    public List<Contact> readDataFromDB()  {
        try {
            contactList = addressBookDBService.readData();
        } catch (AddressBookException e) {
        }
        return contactList;
    }

    // Update address
    public void updateAddress(String name, String newAddress, IOType ioType) {
        if (ioType.equals(IOType.DB_IO)) {
            int result = 0;
            try {
                result = addressBookDBService.updateContactData(name, newAddress);
            } catch (AddressBookException e) {
            }
            if (result == 0) return;
        }
        Contact contactData = this.getContactData(name);
        if (contactData != null) contactData.address = newAddress;
    }

    // Get contacts in date range (exclusively for DB Service)
    public List<Contact> getContactInDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return addressBookDBService.getContactInDateRange(startDate, endDate);
        } catch (AddressBookException e) {
        }
        return null;
    }

    // Get contacts in a city (exclusively for DB Service)
    public Map<String, Integer> getContactInCity() {
        try {
            return addressBookDBService.getContactInCity();
        } catch (AddressBookException e) {
        }
        return null;
    }

    // Add Contact to Address Book ( for all Services)
    public void addContactToAddressBook(Contact contact, IOType ioType) {
        if (ioType.equals(IOType.DB_IO))
            this.addContactToContactTable(contact.firstName, contact.lastName, contact.address, contact.city, contact.state, contact.zip, contact.phoneNo, contact.email, contact.dateAdded);
        else
            contactList.add(contact);
    }

    // Add One Contact to all tables in DB
    public void addContactToEntireDB(String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded, int addressBookId, String[] type) {
        try {
            contactList.add(addressBookDBService.addContactToDB(firstName, lastName, address, city, state, zip, phone, email, dateAdded, addressBookId, type));
        } catch (AddressBookException e) {
        }
    }

    // Add List of Contacts to all tables in DB using Threads
    public void addMultiContactToEntireDBWithThreads(List<Contact> contactList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        contactList.forEach(contactData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(contactData.hashCode(), false);
                System.out.println("Contact Being Added Via Thread: " + Thread.currentThread().getName());
                addContactToEntireDB(contactData.firstName, contactData.lastName, contactData.address, contactData.city, contactData.state, contactData.zip, contactData.phoneNo, contactData.email, contactData.dateAdded, contactData.addressBookId, contactData.type);
                employeeAdditionStatus.put(contactData.hashCode(), true);
                System.out.println("Employee Added Via Thread: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, contactData.firstName);
            thread.start();
        });
        System.out.println("AFTER THREADS OPERATION-------------------------\n" + AddressBookSystem.contactList);
    }

    // Add One Contact to Contact table in DB
    public void addContactToContactTable(String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded) {
        try {
            contactList.add(addressBookDBService.addContact(firstName, lastName, address, city, state, zip, phone, email, dateAdded));
        } catch (AddressBookException e) {
        }
    }

    // Add List of Contacts to Contact table in DB
    public void addMultiContactToContactTable(List<Contact> contactList) {
        contactList.forEach(contactData -> {
            System.out.println("Employee Being Added: " + contactData.firstName);
            addContactToContactTable(contactData.firstName, contactData.lastName, contactData.address, contactData.city, contactData.state, contactData.zip, contactData.phoneNo, contactData.email, contactData.dateAdded);
            System.out.println("Employee Added: " + contactData.firstName);
        });
        System.out.println("AFTER PROCESS OPERATION-------------------------\n" + AddressBookSystem.contactList);
    }

    // Add List of Contacts to Contact table in DB using Threads
    public void addMultiContactToContactTableWithThreads(List<Contact> contactList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        contactList.forEach(contactData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(contactData.hashCode(), false);
                System.out.println("Contact Being Added Via Thread: " + Thread.currentThread().getName());
                addContactToContactTable(contactData.firstName, contactData.lastName, contactData.address, contactData.city, contactData.state, contactData.zip, contactData.phoneNo, contactData.email, contactData.dateAdded);
                employeeAdditionStatus.put(contactData.hashCode(), true);
                System.out.println("Employee Added Via Thread: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, contactData.firstName);
            thread.start();
        });
        System.out.println("AFTER THREADS OPERATION-------------------------\n" + AddressBookSystem.contactList);
    }

    //Delete Contact
    public int deleteContact(String name){
        contactList = contactList.stream().filter(contact -> !contact.firstName.equals(name)).collect(Collectors.toList());
        return contactList.size();
    }

    // Get Contact details, given Name
    public Contact getContactData(String name) {
        return contactList.stream()
                .filter(contacts -> contacts.firstName.equals(name))
                .findFirst()
                .orElse(null);
    }

    // Check if DB is in sync with Local ContactList
    public boolean checkContactInSyncWithDB(String name) {
        List<Contact> contactList = null;
        try {
            contactList = addressBookDBService.getContactData(name);
        } catch (AddressBookException e) {
        }
        return contactList.get(0).equals(getContactData(name));
    }

    // Count entries
    public long countEntries() {
        return contactList.size();
    }
}