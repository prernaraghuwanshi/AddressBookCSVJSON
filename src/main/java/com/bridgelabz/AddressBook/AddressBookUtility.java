package com.bridgelabz.AddressBook;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AddressBookUtility {
    private static Scanner q;
    public static void main(String args[]) throws SQLException {
        System.out.println("Welcome to Address Book Program!");
        AddressBookSystem ads = new AddressBookSystem();
        boolean flag1 = true;
        while (flag1) {
            System.out.println(
                    "Choose what to do in the system\n\n 1. Add Address Book \n 2. Search person by City \n 3. Search person by State \n 4. View Person by City \n "
                            + "5. View Person By State \n 6. Count by City \n 7. Count By State \n 8. Sorted list of names \n 9. Sort by city \n 10. Sort by state \n 11. Sort by ZIP \n"
                            + " 12. Write Data \n" + " 13. Read Data \n" + " 14. Exit \n");
            q = new Scanner(System.in);
            int choice = q.nextInt();
            switch (choice) {
                case 1:
                    // Add address book
                    System.out.println("Enter name of Address Book");
                    Scanner s = new Scanner(System.in);
                    String addressBookName = s.nextLine();
                    if (ads.addressBookMap.containsKey(addressBookName)) {
                        System.out.println("Sorry! Address book " + addressBookName + " already exists!");
                        break;
                    }
                    ads.addAddressBook(addressBookName);
                    break;
                case 2:
                    // Search person by city
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter city name: ");
                    Scanner sc = new Scanner(System.in);
                    String cityName = sc.nextLine();
                    ads.getPersonByCity(cityName);
                    break;
                case 3:
                    // Search person by state
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter state name: ");
                    Scanner s1 = new Scanner(System.in);
                    String stateName = s1.nextLine();
                    ads.getPersonByState(stateName);
                    break;
                case 4:
                    // View Person by city
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter city name: ");
                    Scanner s2 = new Scanner(System.in);
                    String cityName1 = s2.nextLine();
                    ads.viewPersonByCity(cityName1);
                    break;

                case 5:
                    // View Person By State
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter state name: ");
                    Scanner s3 = new Scanner(System.in);
                    String stateName1 = s3.nextLine();
                    ads.viewPersonByState(stateName1);
                    break;
                case 6:
                    // Count by city
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter city name: ");
                    Scanner s4 = new Scanner(System.in);
                    String cityName2 = s4.nextLine();
                    ads.countByCity(cityName2);
                    break;
                case 7:
                    // Count by state
                    if (ads.addressBookMap.size() == 0) {
                        System.out.println("No address book in system!");
                        break;
                    }
                    System.out.println("Enter state name: ");
                    Scanner s5 = new Scanner(System.in);
                    String stateName2 = s5.nextLine();
                    ads.countByState(stateName2);
                    break;
                case 8:
                    // Sort by first name
                    ads.sortByName();
                    break;
                case 9:
                    // Sort by city
                    ads.sortByCity();
                    break;
                case 10:
                    // Sort by state
                    ads.sortByState();
                    break;
                case 11:
                    // Sort by ZIP
                    ads.sortByZip();
                    break;
                case 12:
                    // Write Data
                    Scanner s6 = new Scanner(System.in);
                    System.out.println("Where to write? \n Type 1 for .txt file \n Type 2 for CSV file \n Type 3 for JSON");
                    int option = s6.nextInt();
                    s6.nextLine();
                    System.out.println("Enter name of address book for which you need to write data: ");
                    String nameAddressBook = s6.nextLine();
                    if (option == 1)
                        ads.writeData(nameAddressBook, AddressBookSystem.IOType.FILE_IO);
                    else if (option == 2)
                        ads.writeData(nameAddressBook, AddressBookSystem.IOType.CSV_IO);
                    else if (option == 3)
                        ads.writeData(nameAddressBook, AddressBookSystem.IOType.JSON_IO);
                    break;
                case 13:
                    // Read Data
                    Scanner s7 = new Scanner(System.in);
                    System.out.println("Where to read from? \n Type 1 for .txt file \n Type 2 for CSV file \n Type 3 for JSON");
                    int optionRead = s7.nextInt();
                    s7.nextLine();
                    System.out.println("Enter name of address book for which you need to write data: ");
                    String nameOfAddressBook = s7.nextLine();
                    ArrayList<Contacts> dataFromIO = new ArrayList<Contacts>();
                    if (optionRead == 1)
                        dataFromIO = ads.readData(nameOfAddressBook, AddressBookSystem.IOType.FILE_IO);
                    else if (optionRead == 2)
                        dataFromIO = ads.readData(nameOfAddressBook, AddressBookSystem.IOType.CSV_IO);
                    else if (optionRead == 3)
                        dataFromIO = ads.readData(nameOfAddressBook, AddressBookSystem.IOType.JSON_IO);
                    for (Contacts contact : dataFromIO) {
                        System.out.println("-----Displaying Contact-----");
                        System.out.println("First Name: " + contact.getFirstName());
                        System.out.println("Last Name: " + contact.getLastName());
                        System.out.println("Address: " + contact.getAddress());
                        System.out.println("City: " + contact.getCity());
                        System.out.println("State: " + contact.getState());
                        System.out.println("ZIP Code: " + contact.getZip());
                        System.out.println("Phone Number: " + contact.getPhoneNo());
                        System.out.println("Email: " + contact.getEmail());
                        System.out.println("  ");
                    }
                    break;
                case 14:
                    // Exit
                    System.out.println("-----Exiting from Address Book System-----");
                    flag1 = false;
                    break;
            }
        }
    }
}
