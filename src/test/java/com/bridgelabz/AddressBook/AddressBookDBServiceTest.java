package com.bridgelabz.AddressBook;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class AddressBookDBServiceTest {
    AddressBookSystem addressBookSystem;
    ArrayList<Contacts> contactsList;

    @Before
    public void initialize() {
        addressBookSystem = new AddressBookSystem();
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        List<Contacts> contactList = addressBookSystem.readDataFromDB();
        Assert.assertEquals(4, contactList.size());
    }

    @Test
    public void givenNewAddressForContact_whenUpdated_shouldSyncWithDB() throws SQLException {
        addressBookSystem.readDataFromDB();
        addressBookSystem.updateAddress("Nivedita", "shashtri ward");
        boolean result = addressBookSystem.checkContactInSyncWithDB("Nivedita");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContactDB_whenRetrievingContactsInDateRange_shouldGiveCorrectCount() throws SQLException {
        addressBookSystem.readDataFromDB();
        List<Contacts> contactsListInDateRange = addressBookSystem.getContactInDateRange(LocalDate.of(2019, 01, 01), LocalDate.of(2020, 12, 31));
        Assert.assertEquals(3, contactsListInDateRange.size());
    }

    @Test
    public void givenContactDB_whenRetrievingContactsByCity_shouldGiveCorrectCount() throws SQLException {
        addressBookSystem.readDataFromDB();
        Map<String, Integer> contactsListInCity = addressBookSystem.getContactInCity();
        Assert.assertEquals(3, (int) contactsListInCity.get("Bhopal"));
        Assert.assertEquals(1, (int) contactsListInCity.get("Chennai"));
    }
}
