package com.bridgelabz.AddressBook;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class AddressBookDBServiceTest {
    AddressBookSystem addressBookSystem;
    ArrayList<Contact> contactList;

    @Before
    public void initialize() {
        addressBookSystem = new AddressBookSystem();
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        List<Contact> contactList = addressBookSystem.readDataFromDB();
        Assert.assertEquals(4, contactList.size());
    }

    @Test
    public void givenNewAddressForContact_whenUpdated_shouldSyncWithDB() throws SQLException {
        addressBookSystem.readDataFromDB();
        addressBookSystem.updateAddress("Nivedita", "shashtri ward", AddressBookSystem.IOType.DB_IO);
        boolean result = addressBookSystem.checkContactInSyncWithDB("Nivedita");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContactDB_whenRetrievingContactsInDateRange_shouldGiveCorrectCount() throws SQLException {
        addressBookSystem.readDataFromDB();
        List<Contact> contactListInDateRange = addressBookSystem.getContactInDateRange(LocalDate.of(2019, 01, 01), LocalDate.of(2020, 12, 31));
        Assert.assertEquals(3, contactListInDateRange.size());
    }

    @Test
    public void givenContactDB_whenRetrievingContactsByCity_shouldGiveCorrectCount() throws SQLException {
        addressBookSystem.readDataFromDB();
        Map<String, Integer> contactsListInCity = addressBookSystem.getContactInCity();
        Assert.assertEquals(3, (int) contactsListInCity.get("Bhopal"));
        Assert.assertEquals(1, (int) contactsListInCity.get("Chennai"));
    }

    @Test
    public void givenNewContact_whenAdded_shouldBeInSyncWithDB() throws SQLException {
        addressBookSystem.readDataFromDB();
        addressBookSystem.addContactToEntireDB("Rahul", "Verma", "qwerty lane", "Jaipur", "Rajasthan", "234567", "2345678901", "sdfgth@yahoo.com", LocalDate.now(), 101, new String[]{"Friends", "Family"});
        boolean result = addressBookSystem.checkContactInSyncWithDB("Rekha");
        Assert.assertTrue(result);
    }

    @Test
    public void givenMultipleEmployees_whenAddedToContacts_shouldSyncWithDB() throws SQLException, InterruptedException {
        Contact[] contactArrayWithoutThreads = {
                new Contact(0, "Rakesh", "Arora", "vip road", "Mumbai", "Maharashtra", "322123", "9998547345", "jhj@gmail.com", LocalDate.now()),
                new Contact(0, "Sid", "Mehra", "RTY Bunglow", "Delhi", "Delhi", "400446", "657464329", "smehra@gmail.com", LocalDate.now()),

        };
        Contact[] contactArrayWithThreads = {
                new Contact(0, "Sonal", "Jain", "xyz apartments", "Bengaluru", "Karnataka", "444461", "878786542", "srewd@yahoo.com", LocalDate.now()),
                new Contact(0, "Priyal", "Tyagi", "qwerty lane", "Indore", "MP", "989861", "1111111111", "something@gmail.com", LocalDate.now())
        };
        addressBookSystem.readDataFromDB();
        Instant start = Instant.now();
        addressBookSystem.addMultiContactToContactTable(Arrays.asList(contactArrayWithoutThreads));
        Instant end = Instant.now();
        System.out.println("Duration Without Thread: " + Duration.between(start, end));
        Instant threadStart = Instant.now();
        addressBookSystem.addMultiContactToContactTableWithThreads(Arrays.asList(contactArrayWithThreads));
        Thread.sleep(6000);
        Instant threadEnd = Instant.now();
        System.out.println("Duration With Thread: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(9, addressBookSystem.contactList.size());
    }

    @Test
    public void givenMultipleEmployees_whenAddedToDB_shouldSyncWithDB() throws SQLException, InterruptedException {
        Contact[] contactArrayWithThreads = {
                new Contact(0, "Sonal", "Jain", "xyz apartments", "Bengaluru", "Karnataka", "444461", "878786542", "srewd@yahoo.com", LocalDate.now(),102, new String[]{"Colleague"}),
                new Contact(0, "Priyal", "Tyagi", "qwerty lane", "Indore", "MP", "989861", "1111111111", "something@gmail.com", LocalDate.now(),103, new String[]{"Family", "Friends"})
        };
        addressBookSystem.readDataFromDB();
        Instant threadStart = Instant.now();
        addressBookSystem.addMultiContactToEntireDBWithThreads(Arrays.asList(contactArrayWithThreads));
        Thread.sleep(6000);
        Instant threadEnd = Instant.now();
        System.out.println("Duration With Thread: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(13, addressBookSystem.contactList.size());
    }

}